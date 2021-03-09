package com.syn.issuetracker.service.impl;

import com.syn.issuetracker.model.entity.Notification;
import com.syn.issuetracker.model.enums.Priority;
import com.syn.issuetracker.model.enums.Status;
import com.syn.issuetracker.exception.CustomEntityNotFoundException;
import com.syn.issuetracker.exception.DataConflictException;
import com.syn.issuetracker.exception.UnprocessableEntityException;
import com.syn.issuetracker.service.NotificationService;
import com.syn.issuetracker.specification.TaskSpecification;
import com.syn.issuetracker.model.binding.TaskAddBindingModel;
import com.syn.issuetracker.model.binding.TaskEditBindingModel;
import com.syn.issuetracker.model.entity.Task;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.service.TaskServiceModel;
import com.syn.issuetracker.repository.TaskRepository;
import com.syn.issuetracker.service.UserService;
import com.syn.issuetracker.service.TaskService;
import com.syn.issuetracker.utils.validation.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.*;

import static com.syn.issuetracker.common.ExceptionErrorMessages.*;
import static com.syn.issuetracker.common.MiscConstants.*;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           @Lazy UserService userService,
                           NotificationService notificationService, ModelMapper modelMapper,
                           ValidationUtil validationUtil) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Map<String, Object> getAll(TaskSpecification taskSpecification, Pageable pageable) {
        String userId = taskSpecification.getUserId();
        String title = taskSpecification.getTitle();

        if (this.userService.get(userId).isEmpty()) {
            throw new CustomEntityNotFoundException(NOT_FOUND, List.of(USER_NOT_FOUND));
        }

        List<Task> tasks;
        Page<Task> taskPage;

        if (this.isAdmin(userId)) {
            if (title == null) {
                taskPage = this.taskRepository.findAll(pageable);
            } else {
                taskPage = this.taskRepository.findAllByTitleContaining(title, pageable);
            }
        } else {
            taskPage = this.taskRepository.findAll(taskSpecification, pageable);
        }

        tasks = taskPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put(TASKS, Collections.unmodifiableList(tasks));
        response.put(CURRENT_PAGE, taskPage.getNumber());
        response.put(TOTAL_ITEMS, taskPage.getTotalElements());
        response.put(TOTAL_PAGES, taskPage.getTotalPages());

        return response;
    }

    @Override
    public TaskServiceModel add(TaskAddBindingModel taskAddBindingModel) throws InterruptedException, MessagingException {

        if (!this.validationUtil.isValid(taskAddBindingModel)) {
            throw new UnprocessableEntityException(VALIDATION_FAILURE,
                    this.validationUtil.getViolations(taskAddBindingModel));
        }

        if (this.taskRepository.findByTitle(taskAddBindingModel.getTitle()).isPresent()) {
            throw new DataConflictException(DATA_CONFLICT, List.of(TITLE_ALREADY_EXISTS));
        }

        Task task = this.modelMapper.map(taskAddBindingModel, Task.class);

        this.userService.get(taskAddBindingModel.getDeveloper())
                .map(u -> this.modelMapper.map(u, UserEntity.class))
                .ifPresent(user -> task.setDeveloper(
                        this.modelMapper.map(user, UserEntity.class)));

        task.setCreatedOn(LocalDateTime.now());
        task.setPriority(Priority.values()[taskAddBindingModel.getPriority()]);
        task.setStatus(Status.PENDING);

        this.taskRepository.save(task);

        Notification notification = this.notificationService.create(task);
        this.notificationService.sendNotification(notification);

        return this.modelMapper.map(task, TaskServiceModel.class);
    }

    @Override
    public Optional<TaskServiceModel> get(String taskId) {
        Optional<Task> task = this.taskRepository.findById(taskId);

        return task.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(task.get(), TaskServiceModel.class));
    }

    @Override
    public TaskServiceModel edit(TaskEditBindingModel taskEditBindingModel, String taskId) {

        if (!this.validationUtil.isValid(taskEditBindingModel)) {
            throw new UnprocessableEntityException(VALIDATION_FAILURE,
                    this.validationUtil.getViolations(taskEditBindingModel));
        }

        Task task = this.taskRepository.findById(taskId)
                .map(t -> this.modelMapper.map(t, Task.class))
                .orElseThrow(() -> { throw new CustomEntityNotFoundException(NOT_FOUND, List.of(TASK_NOT_FOUND)); });

        if (notUniqueTitle(taskEditBindingModel.getTitle(), taskId)) {
            throw new DataConflictException(DATA_CONFLICT, List.of(TITLE_ALREADY_EXISTS));
        }

        Optional<UserEntity> user = this.userService.findById(taskEditBindingModel.getDeveloper());

        user.ifPresent(task::setDeveloper);

        task.setTitle(taskEditBindingModel.getTitle());
        task.setDescription(taskEditBindingModel.getDescription());
        task.setPriority(Priority.values()[taskEditBindingModel.getPriority()]);
        task.setStatus(Status.values()[taskEditBindingModel.getStatus()]);

        this.taskRepository.save(task);

        return this.modelMapper.map(task, TaskServiceModel.class);
    }

    @Override
    public void delete(String taskId) {
        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    throw new CustomEntityNotFoundException(NOT_FOUND, List.of(TASK_NOT_FOUND));
                });

        this.taskRepository.delete(task);
    }

    @Override
    public void deleteAll(String userId) {

        this.userService.findById(userId)
                .orElseThrow(() -> { throw new CustomEntityNotFoundException(NOT_FOUND, List.of(USER_NOT_FOUND)); });

        if (this.isAdmin(userId)) {
            this.taskRepository.deleteAll();
        }

        this.taskRepository.deleteAll(this.taskRepository
                .getAllByUserId(userId));
    }

    @Override
    public void unAssignTasks(String userId) {

        this.taskRepository.getAllByUserId(userId).forEach(task -> {
            task.setDeveloper(null);
            this.taskRepository.save(task);
        });
    }

    private boolean isAdmin(String userId) {
        return this.userService.isAdmin(userId);
    }

    private boolean notUniqueTitle(String title, String taskId) {
        Optional<Task> found = this.taskRepository.findByTitle(title);
        return found.isPresent() && !found.get().getId().equals(taskId);
    }
}
