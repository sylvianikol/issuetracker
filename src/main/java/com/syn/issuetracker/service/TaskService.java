package com.syn.issuetracker.service;

import com.syn.issuetracker.specification.TaskSpecification;
import com.syn.issuetracker.model.binding.TaskAddBindingModel;
import com.syn.issuetracker.model.binding.TaskEditBindingModel;
import com.syn.issuetracker.model.service.TaskServiceModel;

import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.util.Map;
import java.util.Optional;

public interface TaskService {

    Map<String, Object> getAll(TaskSpecification taskSpecification, Pageable pageable);

    TaskServiceModel add(TaskAddBindingModel taskAddBindingModel) throws InterruptedException, MessagingException;

    Optional<TaskServiceModel> get(String taskId);

    TaskServiceModel edit(TaskEditBindingModel taskEditBindingModel, String taskId);

    void delete(String taskId);

    void deleteAll(String userId);

    void unAssignTasks(String id);

}
