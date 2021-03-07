package com.syn.issuetracker.web;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static com.syn.issuetracker.common.ExceptionErrorMessages.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TasksControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    private String TASK_1_ID;
    private String USER_ID;

    @BeforeEach
    public void setUp() {
        this.tearDown();

        UserRoleEntity role = this.userRoleRepository.findByRole(UserRole.ROLE_ADMIN);
        if (role == null) {
            role = new UserRoleEntity(UserRole.ROLE_ADMIN);
            this.userRoleRepository.save(role);
        }

        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setEmail("test@mail.com");
        user.setPassword("123");
        user.setAuthorities(List.of(role));

        this.userRepository.save(user);

        Task task1 = new Task();
        task1.setTitle("Test task");
        task1.setDescription("Description test");
        task1.setPriority(Priority.LOW);
        task1.setStatus(Status.PENDING);
        task1.setCreatedOn(LocalDateTime.now());
        task1.setDeveloper(user);

        this.taskRepository.save(task1);

        TASK_1_ID = task1.getId();
        USER_ID = user.getId();
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

    @WithMockUser(username="testUser")
    @Test
    void test_getAll_isOK() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .param("userId", USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks", hasSize(1)))
                .andExpect(jsonPath("$.tasks.[0].id", is(TASK_1_ID)))
                .andExpect(jsonPath("$.tasks.[0].developer.id", is(USER_ID)));
    }

    @WithMockUser(username="testUser")
    @Test
    void test_getAll_isNotFound() throws Exception {
        this.taskRepository.deleteAll();
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .param("userId", USER_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username="testUser")
    @Test
    void test_getAll_userNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .param("userId", "999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(NOT_FOUND)));
    }
}