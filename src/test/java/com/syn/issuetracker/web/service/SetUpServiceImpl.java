package com.syn.issuetracker.web.service;

import com.syn.issuetracker.model.binding.TaskAddBindingModel;
import com.syn.issuetracker.model.binding.TaskEditBindingModel;
import com.syn.issuetracker.model.entity.Task;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.entity.UserRoleEntity;
import com.syn.issuetracker.model.enums.Priority;
import com.syn.issuetracker.model.enums.Status;
import com.syn.issuetracker.model.enums.UserRole;
import com.syn.issuetracker.model.payload.request.LoginRequest;
import com.syn.issuetracker.model.payload.request.SignUpRequest;
import com.syn.issuetracker.repository.TaskRepository;
import com.syn.issuetracker.repository.UserRepository;
import com.syn.issuetracker.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Service
public class SetUpServiceImpl implements SetUpService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void tearDown() {
        this.taskRepository.deleteAll();
        this.userRepository.deleteAll();
        this.userRoleRepository.deleteAll();
    }

    @Override
    public void injectedComponentsAreNotNull() {
        assertThat(this.taskRepository).isNotNull();
        assertThat(this.userRepository).isNotNull();
        assertThat(this.userRoleRepository).isNotNull();
    }

    @Override
    public UserRoleEntity createUserRole(UserRole userRole) {
        UserRoleEntity role = new UserRoleEntity(userRole);
        this.userRoleRepository.save(role);
        return role;
    }

    @Override
    public UserEntity createUser(List<UserRoleEntity> roles, String username, String email, String password) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setAuthorities(roles);
        this.userRepository.save(user);
        return user;
    }

    @Override
    public Task createTask(UserEntity user, String title, String desc) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(desc);
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.PENDING);
        task.setCreatedOn(LocalDateTime.now());
        task.setDeveloper(user);
        this.taskRepository.save(task);
        return task;
    }

    @Override
    public TaskAddBindingModel createTaskToAdd(String title, String desc, String userId) {
        TaskAddBindingModel taskToAdd = new TaskAddBindingModel();
        taskToAdd.setTitle(title);
        taskToAdd.setDescription(desc);
        taskToAdd.setPriority(0);
        taskToAdd.setDeveloper(userId);
        return taskToAdd;
    }

    @Override
    public TaskEditBindingModel createTaskToEdit(String title, String desc, int priority, String developer, int status) {
        TaskEditBindingModel taskToEdit = new TaskEditBindingModel();
        taskToEdit.setTitle(title);
        taskToEdit.setDescription(desc);
        taskToEdit.setPriority(priority);
        taskToEdit.setDeveloper(developer);
        taskToEdit.setStatus(status);
        return taskToEdit;
    }

    @Override
    public void deleteAllTasks() {
        this.taskRepository.deleteAll();
    }

    @Override
    public boolean tasksEmpty() {
        return this.taskRepository.count() == 0;
    }

    @Override
    public long countTasks() {
        return this.taskRepository.count();
    }

    @Override
    public SignUpRequest createSignUpRequest(String user, String email, String password) {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(user);
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);
        return signUpRequest;
    }

    @Override
    public LoginRequest createLoginRequest(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        return loginRequest;
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }
}
