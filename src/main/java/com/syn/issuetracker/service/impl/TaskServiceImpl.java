package com.syn.issuetracker.service.impl;

import com.syn.issuetracker.enums.Priority;
import com.syn.issuetracker.enums.Status;
import com.syn.issuetracker.exception.CustomEntityNotFoundException;
import com.syn.issuetracker.exception.DataConflictException;
import com.syn.issuetracker.exception.UnprocessableEntityException;
import com.syn.issuetracker.model.service.UserServiceModel;
import com.syn.issuetracker.specification.TaskSpecification;
import com.syn.issuetracker.model.binding.TaskAddBindingModel;
import com.syn.issuetracker.model.binding.TaskEditBindingModel;
import com.syn.issuetracker.model.entity.Task;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.service.TaskServiceModel;
import com.syn.issuetracker.repository.TaskRepository;
import com.syn.issuetracker.service.UserService;
import com.syn.issuetracker.service.TaskService;
import com.syn.issuetracker.utils.ValidationUtil;
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

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           @Lazy UserService userService,
                           ModelMapper modelMapper,
                           ValidationUtil validationUtil) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Map<String, Object> getAll(TaskSpecification taskSpecification, Pageable pageable) {
        String userId = taskSpecification.getUserId();
        String title = taskSpecification.getTitle();

        if (this.userService.get(userId).isEmpty()) {
            throw new CustomEntityNotFoundException(USER_NOT_FOUND);
        }

        List<Task> tasks;
        Page<Task> taskPage;

        if (this.isAdmin(userId)) {
            if (title == null) {
                taskPage = this.taskRepository.findAll(pageable);
            } else {
                taskPage = this.taskRepository.findAllByTitle(title, pageable);
            }
        } else {
            taskPage = this.taskRepository.findAll(taskSpecification, pageable);
        }

        tasks = taskPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("tasks", Collections.unmodifiableList(tasks));
        response.put("currentPage", taskPage.getNumber());
        response.put("totalItems", taskPage.getTotalElements());
        response.put("totalPages", taskPage.getTotalPages());

        return response;
    }

    @Override
    public TaskServiceModel add(TaskAddBindingModel taskAddBindingModel) throws InterruptedException, MessagingException {

        if (!this.validationUtil.isValid(taskAddBindingModel)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED);
        }

        String title = taskAddBindingModel.getTitle();
        if (this.taskRepository.findByTitle(title).isPresent()) {
            throw new DataConflictException(TITLE_ALREADY_EXISTS);
        }

        Task task = this.modelMapper.map(taskAddBindingModel, Task.class);

        this.userService.get(taskAddBindingModel.getDeveloper())
                .map(u -> this.modelMapper.map(u, UserEntity.class))
                .ifPresent(user -> task.setDeveloper(
                        this.modelMapper.map(user, UserEntity.class)));

        task.setCreatedOn(LocalDateTime.now());
        task.setStatus(Status.PENDING);

        this.taskRepository.save(task);

//        NotificationExecutorFactory.getExecutor(NotificationType.EMAIL)
//                .sendNotification(user.getUsername(), user.getEmail(), task.getTitle(),
//                        task.getDescription(), task.getPriority().toString(), task.getId());

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

        Task task = this.taskRepository.findById(taskId)
                .map(t -> this.modelMapper.map(t, Task.class))
                .orElseThrow(() -> {
                    throw new CustomEntityNotFoundException(TASK_NOT_FOUND);
                });

        String title = taskEditBindingModel.getTitle();
        Optional<Task> taskTitle = this.taskRepository.findByTitle(title);
        if (taskTitle.isPresent() && !taskTitle.get().getId().equals(taskId)) {
            throw new DataConflictException(TITLE_ALREADY_EXISTS);
        }

        Optional<UserEntity> user = this.userService.findById(taskEditBindingModel.getDeveloperId());

        user.ifPresent(task::setDeveloper);

        task.setTitle(taskEditBindingModel.getTitle());
        task.setDescription(taskEditBindingModel.getDescription());
        task.setPriority(Priority.valueOf(taskEditBindingModel.getPriority()));
        task.setStatus(Status.valueOf(taskEditBindingModel.getStatus()));

        this.taskRepository.save(task);

        return this.modelMapper.map(task, TaskServiceModel.class);
    }

    @Override
    public void delete(String taskId) {
        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    throw new CustomEntityNotFoundException(TASK_NOT_FOUND);
                });

        this.taskRepository.delete(task);
    }

    @Override
    public void deleteAll(String userId) {

        if (this.isAdmin(userId)) {
            this.taskRepository.deleteAll();
        }

        this.taskRepository.deleteAll(this.taskRepository
                .getAllByUserId(userId));
    }

    @Override
    public void unassignTasks(String userId) {

        this.taskRepository.getAllByUserId(userId).forEach(task -> {
            task.setDeveloper(null);
            this.taskRepository.save(task);
        });
    }


    private boolean isAdmin(String userId) {
        return this.userService.isAdmin(userId);
    }
}
