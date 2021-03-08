package com.syn.issuetracker.web;

import com.syn.issuetracker.model.binding.TaskAddBindingModel;
import com.syn.issuetracker.model.binding.TaskEditBindingModel;
import com.syn.issuetracker.model.entity.Task;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.entity.UserRoleEntity;
import com.syn.issuetracker.model.enums.Priority;
import com.syn.issuetracker.model.enums.Status;
import com.syn.issuetracker.model.enums.UserRole;
import com.syn.issuetracker.repository.TaskRepository;

import com.syn.issuetracker.repository.UserRepository;
import com.syn.issuetracker.repository.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static com.syn.issuetracker.common.ExceptionErrorMessages.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TasksControllerTest extends JsonMapper {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    private UserEntity ADMIN;
    private UserEntity USER;
    private String ADMIN_ID;
    private String USER_ID;
    private String TASK1_ID;
    private String TASK2_ID;
    private String TASK1_TITLE;

    @BeforeEach
    public void setUp() {
        this.tearDown();

        UserRoleEntity roleAdmin = this.createUserRole(UserRole.ROLE_ADMIN);
        UserRoleEntity roleUser = this.createUserRole(UserRole.ROLE_USER);

        ADMIN = this.createUser(List.of(roleAdmin, roleUser), "admin", "admin@mail.com", "123");
        USER = this.createUser(List.of(roleUser), "user", "user@mail.com", "123");

        this.userRepository.save(ADMIN);
        this.userRepository.save(USER);

        Task task1 = this.createTask(ADMIN, "Test task", "Description test");
        Task task2 = this.createTask(USER, "Another Test task", "Description test");

        this.taskRepository.save(task1);
        this.taskRepository.save(task2);

        TASK1_ID = task1.getId();
        TASK2_ID = task2.getId();
        TASK1_TITLE = task1.getTitle();
        ADMIN_ID = ADMIN.getId();
        USER_ID = USER.getId();
    }

    @AfterEach
    public void tearDown() {
        this.taskRepository.deleteAll();
        this.userRepository.deleteAll();
        this.userRoleRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(this.taskRepository).isNotNull();
        assertThat(this.userRepository).isNotNull();
        assertThat(this.userRoleRepository).isNotNull();
    }

    @WithMockUser(username="admin")
    @Test
    void test_getAll_isOK() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .param("userId", ADMIN_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks", hasSize(2)));
    }

    @WithMockUser(username="admin")
    @Test
    void test_getAll_isNotFound() throws Exception {
        this.taskRepository.deleteAll();
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .param("userId", ADMIN_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username="admin")
    @Test
    void test_getAll_userNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .param("userId", "999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(NOT_FOUND)));
    }

    @WithMockUser(username="admin")
    @Test
    void test_get_isOK() throws Exception {
        this.mockMvc.perform(get("/tasks/{taskId}", TASK1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TASK1_ID)));
    }

    @WithMockUser(username="admin")
    @Test
    void test_get_isNotFound() throws Exception {
        this.mockMvc.perform(get("/tasks/{taskId}", "999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username="admin")
    @Test
    void test_add_isOk() throws Exception {

        TaskAddBindingModel taskToAdd = this.createTaskToAdd("New Task", "New Desc");

        String inputJson = super.mapToJson(taskToAdd);

        this.mockMvc.perform(post("/tasks/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username="admin")
    @Test
    void test_add_isUnprocessable_whenInvalidTitle() throws Exception {

        TaskAddBindingModel taskToAdd = this.createTaskToAdd("", "New Desc");

        String inputJson = super.mapToJson(taskToAdd);

        this.mockMvc.perform(post("/tasks/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.description", is(VALIDATION_FAILURE)))
                .andExpect(jsonPath("$.error.[0]", containsString("Title")))
                .andExpect(jsonPath("$.error.[1]", containsString("Title")));
    }

    @WithMockUser(username="admin")
    @Test
    void test_add_isConflict_whenTitleExists() throws Exception {

        TaskAddBindingModel taskToAdd = this.createTaskToAdd(TASK1_TITLE, "New Desc");

        String inputJson = super.mapToJson(taskToAdd);

        this.mockMvc.perform(post("/tasks/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is(DATA_CONFLICT)))
                .andExpect(jsonPath("$.error.[0]", is(TITLE_ALREADY_EXISTS)));
    }

    @WithMockUser(username="admin")
    @Test
    void test_edit_isOk() throws Exception {

        TaskEditBindingModel taskToEdit = this.createTaskToEdit(
                "Edited Title", "Edited Desc", 2, USER_ID, 2);

        String inputJson = super.mapToJson(taskToEdit);

        this.mockMvc.perform(post("/tasks/{taskId}", TASK1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username="admin")
    @Test
    void test_edit_isUnprocessable_whenInvalidTitle() throws Exception {

        TaskEditBindingModel taskToEdit = this.createTaskToEdit(
                "", "Edited Desc", 2, USER_ID, 2);

        String inputJson = super.mapToJson(taskToEdit);

        this.mockMvc.perform(post("/tasks/{taskId}", TASK1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.description", is(VALIDATION_FAILURE)))
                .andExpect(jsonPath("$.error.[0]", containsString("Title")))
                .andExpect(jsonPath("$.error.[1]", containsString("Title")));
    }

    @WithMockUser(username="admin")
    @Test
    void test_edit_isNotFound() throws Exception {

        TaskEditBindingModel taskToEdit = this.createTaskToEdit(
                "Edited Title", "Edited Desc", 2, USER_ID, 2);

        String inputJson = super.mapToJson(taskToEdit);

        this.mockMvc.perform(post("/tasks/{taskId}", "999")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(NOT_FOUND)))
                .andExpect(jsonPath("$.error.[0]", is(TASK_NOT_FOUND)));
    }

    @WithMockUser(username="admin")
    @Test
    void test_edit_isConflict_whenNotUniqueTitle() throws Exception {

        TaskEditBindingModel taskToEdit = this.createTaskToEdit(
                TASK1_TITLE, "Edited Desc", 2, USER_ID, 2);

        String inputJson = super.mapToJson(taskToEdit);

        this.mockMvc.perform(post("/tasks/{taskId}", TASK2_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is(DATA_CONFLICT)))
                .andExpect(jsonPath("$.error.[0]", is(TITLE_ALREADY_EXISTS)));
    }

    @WithMockUser(username="admin")
    @Test
    void test_delete_isOk() throws Exception {

        this.mockMvc.perform(delete("/tasks/{taskId}", TASK1_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username="admin")
    @Test
    void test_delete_isNotFound() throws Exception {

        this.mockMvc.perform(delete("/tasks/{taskId}", "999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username="admin")
    @Test
    void test_deleteAll_isOk() throws Exception {

        this.mockMvc.perform(delete("/tasks")
                .param("userId", ADMIN_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username="admin")
    @Test
    void test_deleteAll_deletesAllWhenAdmin() throws Exception {

        this.mockMvc.perform(delete("/tasks")
                .param("userId", ADMIN_ID));

        assertThat(this.taskRepository.count() == 0);
    }

    @WithMockUser(username="user")
    @Test
    void test_deleteAll_deletesOnlyUserTasks_whenUser() throws Exception {

        this.mockMvc.perform(delete("/tasks")
                .param("userId", USER_ID));

        assertThat(this.taskRepository.count() == 1);
    }

    //////  *** ////////  *** ////////  *** ////////  *** ////////
    private UserRoleEntity createUserRole(UserRole userRole) {
        UserRoleEntity role = new UserRoleEntity(userRole);
        this.userRoleRepository.save(role);

        return role;
    }

    private UserEntity createUser(List<UserRoleEntity> roles, String name, String email, String password) {
        UserEntity user = new UserEntity();
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setAuthorities(roles);
        return user;
    }

    private Task createTask(UserEntity user, String title, String desc) {
        Task task1 = new Task();
        task1.setTitle(title);
        task1.setDescription(desc);
        task1.setPriority(Priority.HIGH);
        task1.setStatus(Status.PENDING);
        task1.setCreatedOn(LocalDateTime.now());
        task1.setDeveloper(user);
        return task1;
    }

    private TaskAddBindingModel createTaskToAdd(String title, String desc) {
        TaskAddBindingModel taskToAdd = new TaskAddBindingModel();
        taskToAdd.setTitle(title);
        taskToAdd.setDescription(desc);
        taskToAdd.setPriority(0);
        taskToAdd.setDeveloper(ADMIN_ID);
        return taskToAdd;
    }

    private TaskEditBindingModel createTaskToEdit(String title, String desc, int priority, String developer, int status) {
        TaskEditBindingModel taskToEdit = new TaskEditBindingModel();
        taskToEdit.setTitle(title);
        taskToEdit.setDescription(desc);
        taskToEdit.setPriority(priority);
        taskToEdit.setDeveloper(developer);
        taskToEdit.setStatus(status);
        return taskToEdit;
    }
}