package com.syn.issuetracker.web.service;

import com.syn.issuetracker.model.binding.TaskAddBindingModel;
import com.syn.issuetracker.model.binding.TaskEditBindingModel;
import com.syn.issuetracker.model.binding.UserEditBindingModel;
import com.syn.issuetracker.model.binding.UserRoleEntityBindingModel;
import com.syn.issuetracker.model.entity.Task;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.entity.UserRoleEntity;
import com.syn.issuetracker.model.enums.UserRole;
import com.syn.issuetracker.model.payload.request.LoginRequest;
import com.syn.issuetracker.model.payload.request.SignUpRequest;
import com.syn.issuetracker.repository.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetUpService {

    void tearDown();

    void injectedComponentsAreNotNull();

    UserRoleEntity createUserRole(UserRole userRole);

    UserEntity createUser(List<UserRoleEntity> roles, String name, String email, String password);

    Task createTask(UserEntity user, String title, String desc);

    TaskAddBindingModel createTaskToAdd(String title, String desc, String userId);

    TaskEditBindingModel createTaskToEdit(String title, String desc, int priority, String developer, int status);

    UserRoleEntityBindingModel createRoleToEdit(UserRole role);

    UserEditBindingModel createUserToEdit(String username, String email, List<UserRoleEntityBindingModel> authorities);

    void deleteAllTasks();

    void deleteAllUsers();

    boolean tasksEmpty();

    long countTasks();

    SignUpRequest createSignUpRequest(String user, String email, String password);

    LoginRequest createLoginRequest(String username, String password);

    UserEntity getUserByUsername(String username);
}
