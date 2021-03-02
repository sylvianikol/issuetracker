package com.syn.issuetracker.service.impl;

import com.syn.issuetracker.enums.Priority;
import com.syn.issuetracker.exception.CustomEntityNotFoundException;
import com.syn.issuetracker.exception.DataConflictException;
import com.syn.issuetracker.model.binding.TaskAddBindingModel;
import com.syn.issuetracker.model.binding.TaskEditBindingModel;
import com.syn.issuetracker.model.entity.Task;
import com.syn.issuetracker.model.service.TaskServiceModel;
import com.syn.issuetracker.repository.TaskRepository;
import com.syn.issuetracker.service.UserService;
import com.syn.issuetracker.service.TaskService;
import com.syn.issuetracker.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.issuetracker.common.ExceptionErrorMessages.*;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public List<TaskServiceModel> getAll(String title, String userId) {
        // todo: check if user exists

        // todo: if user is admin
        //  if title == null -> return findAllByAndOrderByCreatedOnAsc()
        //  else -> findAllByTitleAndOrderByCreatedOnAsc(title)

        if (title == null) {
            return this.taskRepository
                    .findAllByUserIdAndOrderByCreatedOnAsc(userId)
                    .stream()
                    .map(task -> this.modelMapper.map(task, TaskServiceModel.class))
                    .collect(Collectors.toUnmodifiableList());
        } else {
            return this.taskRepository.findByUserIdAndTitleContaining(userId, title).stream()
                    .map(task -> this.modelMapper.map(task, TaskServiceModel.class))
                    .collect(Collectors.toUnmodifiableList());
        }
    }

    @Override
    public TaskServiceModel add(TaskAddBindingModel taskAddBindingModel) {

//        if (!this.validationUtil.isValid(taskAddBindingModel)) {
//            throw new UnprocessableEntityException(VALIDATION_FAILED,
//                    this.validationUtil.getViolations(taskAddBindingModel));
//        }

        String title = taskAddBindingModel.getTitle();
        if (this.taskRepository.findByTitle(title).isPresent()) {
            throw new DataConflictException(TITLE_ALREADY_EXISTS);
        }

        Task task = this.modelMapper.map(taskAddBindingModel, Task.class);
        task.setCreatedOn(LocalDateTime.now());

        this.taskRepository.save(task);

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

        task.setTitle(taskEditBindingModel.getTitle());
        task.setDescription(taskEditBindingModel.getDescription());
        task.setPriority(Priority.valueOf(taskEditBindingModel.getPriority()));
        task.setCompleted(taskEditBindingModel.isCompleted());

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
    public void deleteAll() {
        this.taskRepository.deleteAll();
    }
}
