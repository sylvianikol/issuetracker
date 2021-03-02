package com.syn.issuetracker.service;

import com.syn.issuetracker.model.binding.TaskAddBindingModel;
import com.syn.issuetracker.model.binding.TaskEditBindingModel;
import com.syn.issuetracker.model.service.TaskServiceModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskService {

    List<TaskServiceModel> getAll(String title, String userId);

    TaskServiceModel add(TaskAddBindingModel taskAddBindingModel);

    Optional<TaskServiceModel> get(String taskId);

    TaskServiceModel edit(TaskEditBindingModel taskEditBindingModel, String taskId);

    void delete(String taskId);

    void deleteAll();
}
