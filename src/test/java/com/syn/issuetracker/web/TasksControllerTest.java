package com.syn.issuetracker.web;

import com.syn.issuetracker.common.ValidationErrorMessages;
import com.syn.issuetracker.model.binding.TaskAddBindingModel;
import com.syn.issuetracker.model.binding.TaskEditBindingModel;
import com.syn.issuetracker.model.entity.Task;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.entity.UserRoleEntity;
import com.syn.issuetracker.model.enums.UserRole;

import com.syn.issuetracker.web.service.SetUpService;
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

import java.util.List;

import static com.syn.issuetracker.common.ExceptionErrorMessages.*;
import static com.syn.issuetracker.common.ValidationErrorMessages.TITLE_BLANK;
import static com.syn.issuetracker.common.ValidationErrorMessages.TITLE_LENGTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
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
    private SetUpService setUp;

    private String ADMIN_ID;
    private String USER_ID;
    private String TASK1_ID;
    private String TASK2_ID;
    private String TASK1_TITLE;

    @BeforeEach
    public void init() {
        this.setUp.tearDown();

        UserRoleEntity roleAdmin = this.setUp.createUserRole(UserRole.ROLE_ADMIN);
        UserRoleEntity roleUser = this.setUp.createUserRole(UserRole.ROLE_USER);

        UserEntity admin = this.setUp
                .createUser(List.of(roleAdmin, roleUser), "admin", "admin@mail.com", "123");
        UserEntity user = this.setUp
                .createUser(List.of(roleUser), "user", "user@mail.com", "123");

        Task task1 = this.setUp.createTask(admin, "Test task", "Description test");
        Task task2 = this.setUp.createTask(user, "Another Test task", "Description test");

        TASK1_ID = task1.getId();
        TASK2_ID = task2.getId();
        TASK1_TITLE = task1.getTitle();
        ADMIN_ID = admin.getId();
        USER_ID = user.getId();
    }

    @AfterEach
    public void tearDown() {
        this.setUp.tearDown();
    }

    @Test
    void injectedComponentsAreNotNull() {
        this.setUp.injectedComponentsAreNotNull();
    }

    @WithMockUser(username="admin")
    @Test
    void test_getAll_isOk_getsAllWhenAdmin() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .param("userId", ADMIN_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks", hasSize(2)));
    }

    @WithMockUser(username="user")
    @Test
    void test_getAll_isOk_getsUserTasksWhenUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .param("userId", USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks", hasSize(1)));
    }

    @WithMockUser(username="admin")
    @Test
    void test_getAll_isNotFound() throws Exception {
        this.setUp.deleteAllTasks();
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

        TaskAddBindingModel taskToAdd = this.setUp
                .createTaskToAdd("New Task", "New Desc", ADMIN_ID);

        String inputJson = super.mapToJson(taskToAdd);

        this.mockMvc.perform(post("/tasks/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username="admin")
    @Test
    void test_add_isUnprocessable_whenInvalidTitle() throws Exception {

        TaskAddBindingModel taskToAdd = this.setUp.createTaskToAdd("", "New Desc", ADMIN_ID);

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

        TaskAddBindingModel taskToAdd = this.setUp.createTaskToAdd(TASK1_TITLE, "New Desc", ADMIN_ID);

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

        TaskEditBindingModel taskToEdit = this.setUp.createTaskToEdit(
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

        TaskEditBindingModel taskToEdit = this.setUp.createTaskToEdit(
                "", "Edited Desc", 2, USER_ID, 2);

        String inputJson = super.mapToJson(taskToEdit);

        this.mockMvc.perform(post("/tasks/{taskId}", TASK1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.description", is(VALIDATION_FAILURE)))
                .andExpect(jsonPath("$.error", hasSize(2)))
                .andExpect(jsonPath("$.error", hasItem(TITLE_BLANK)))
                .andExpect(jsonPath("$.error", hasItem(TITLE_LENGTH)));
    }

    @WithMockUser(username="admin")
    @Test
    void test_edit_isNotFound() throws Exception {

        TaskEditBindingModel taskToEdit = this.setUp.createTaskToEdit(
                "Edited Title", "Edited Desc", 2, USER_ID, 2);

        String inputJson = super.mapToJson(taskToEdit);

        this.mockMvc.perform(post("/tasks/{taskId}", "999")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(NOT_FOUND)))
                .andExpect(jsonPath("$.error", hasItem(TASK_NOT_FOUND)));
    }

    @WithMockUser(username="admin")
    @Test
    void test_edit_isConflict_whenNotUniqueTitle() throws Exception {

        TaskEditBindingModel taskToEdit = this.setUp.createTaskToEdit(
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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(NOT_FOUND)))
                .andExpect(jsonPath("$.error", hasItem(TASK_NOT_FOUND)));
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

        assertThat(this.setUp.tasksEmpty());
    }

    @WithMockUser(username="user")
    @Test
    void test_deleteAll_deletesOnlyUserTasks_whenUser() throws Exception {

        this.mockMvc.perform(delete("/tasks")
                .param("userId", USER_ID));

        assertThat(this.setUp.countTasks() == 1);
    }
}