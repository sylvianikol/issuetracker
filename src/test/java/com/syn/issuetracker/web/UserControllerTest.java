package com.syn.issuetracker.web;

import com.syn.issuetracker.model.binding.UserEditBindingModel;
import com.syn.issuetracker.model.binding.UserRoleEntityBindingModel;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.entity.UserRoleEntity;
import com.syn.issuetracker.model.enums.UserRole;

import com.syn.issuetracker.web.util.SetUpUtil;
import com.syn.issuetracker.web.util.JsonMapper;
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
import static com.syn.issuetracker.common.ValidationErrorMessages.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends JsonMapper {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SetUpUtil setUp;

    private String USER_ID;
    private String USERNAME;
    private String ADMIN_USERNAME;
    private String ADMIN_EMAIL;
    private String EMAIL;

    @BeforeEach
    public void init() {
        this.setUp.tearDown();

        UserRoleEntity roleAdmin = this.setUp.createUserRole(UserRole.ROLE_ADMIN);
        UserRoleEntity roleUser = this.setUp.createUserRole(UserRole.ROLE_USER);

        UserEntity admin = this.setUp
                .createUser(List.of(roleAdmin, roleUser), "admin", "admin@mail.com", "123");
        UserEntity user = this.setUp
                .createUser(List.of(roleUser), "user", "user@mail.com", "123");

        USER_ID = user.getId();
        USERNAME = user.getUsername();
        ADMIN_USERNAME = admin.getUsername();
        ADMIN_EMAIL = admin.getEmail();
        EMAIL = user.getEmail();
    }

    @AfterEach
    public void tearDown() {
        this.setUp.tearDown();
    }

    @Test
    void injectedComponentsAreNotNull() {
        this.setUp.injectedComponentsAreNotNull();
    }

    @WithMockUser(username="user")
    @Test
    void test_getAll_isForbidden() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/users"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="admin", roles = {"ADMIN"})
    @Test
    void test_getAll_isOK() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(2)));
    }

    @WithMockUser(username="admin", roles = {"ADMIN"})
    @Test
    void test_getAll_isNotFound() throws Exception {
        this.setUp.deleteAllUsers();
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/users"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username="user")
    @Test
    void test_get_isForbidden() throws Exception {
        this.mockMvc.perform(get("/users/{userId}", USER_ID))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="admin", roles = {"ADMIN"})
    @Test
    void test_get_isOk() throws Exception {
        this.mockMvc.perform(get("/users/{userId}", USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USER_ID)));
    }

    @WithMockUser(username="admin", roles = {"ADMIN"})
    @Test
    void test_get_isNotFound() throws Exception {
        this.mockMvc.perform(get("/users/{userId}", "999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username="user")
    @Test
    void test_edit_isForbidden() throws Exception {

        UserRoleEntityBindingModel editedRole = this.setUp.createRoleToEdit(UserRole.ROLE_USER);

        UserEditBindingModel userToEdit = this.setUp
                .createUserToEdit("editedUsername", "edited@mail.com", List.of(editedRole));

        String inputJson = super.mapToJson(userToEdit);

        this.mockMvc.perform(post("/users/{userId}", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="admin", roles = {"ADMIN"})
    @Test
    void test_edit_isOk() throws Exception {

        UserRoleEntityBindingModel editedRole = this.setUp.createRoleToEdit(UserRole.ROLE_USER);

        UserEditBindingModel userToEdit = this.setUp
                .createUserToEdit("editedUsername", "edited@mail.com", List.of(editedRole));

        String inputJson = super.mapToJson(userToEdit);

        this.mockMvc.perform(post("/users/{userId}", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username="admin", roles = {"ADMIN"})
    @Test
    void test_isUnprocessable_whenInvalidData() throws Exception {

        UserRoleEntityBindingModel editedRole = this.setUp.createRoleToEdit(UserRole.ROLE_USER);

        UserEditBindingModel userToEdit = this.setUp
                .createUserToEdit("", "", List.of(editedRole));

        String inputJson = super.mapToJson(userToEdit);

        this.mockMvc.perform(post("/users/{userId}", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.description", is(VALIDATION_FAILURE)))
                .andExpect(jsonPath("$.error", hasSize(3)))
                .andExpect(jsonPath("$.error", hasItem(USERNAME_BLANK)))
                .andExpect(jsonPath("$.error", hasItem(USERNAME_LENGTH)))
                .andExpect(jsonPath("$.error", hasItem(EMAIL_BLANK)));
    }

    @WithMockUser(username="admin", roles = {"ADMIN"})
    @Test
    void test_isUnprocessable_whenInvalidEmail() throws Exception {

        UserRoleEntityBindingModel editedRole = this.setUp.createRoleToEdit(UserRole.ROLE_USER);

        UserEditBindingModel userToEdit = this.setUp
                .createUserToEdit("editedUsername", "invalid.email", List.of(editedRole));

        String inputJson = super.mapToJson(userToEdit);

        this.mockMvc.perform(post("/users/{userId}", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.description", is(VALIDATION_FAILURE)))
                .andExpect(jsonPath("$.error", hasSize(1)))
                .andExpect(jsonPath("$.error", hasItem(EMAIL_NOT_VALID )));
    }

    @WithMockUser(username="admin", roles = {"ADMIN"})
    @Test
    void test_isNotFound() throws Exception {

        UserRoleEntityBindingModel editedRole = this.setUp.createRoleToEdit(UserRole.ROLE_USER);

        UserEditBindingModel userToEdit = this.setUp
                .createUserToEdit("editedUsername", "edited@mail.com", List.of(editedRole));

        String inputJson = super.mapToJson(userToEdit);

        this.mockMvc.perform(post("/users/{userId}", "999")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(NOT_FOUND)))
                .andExpect(jsonPath("$.error.[0]", is(USER_NOT_FOUND)));
    }

    @WithMockUser(username="admin", roles = {"ADMIN"})
    @Test
    void test_isConflict_whenUsernameUsed() throws Exception {

        UserRoleEntityBindingModel editedRole = this.setUp.createRoleToEdit(UserRole.ROLE_USER);

        UserEditBindingModel userToEdit = this.setUp
                .createUserToEdit(ADMIN_USERNAME, "edited@mail.com", List.of(editedRole));

        String inputJson = super.mapToJson(userToEdit);

        this.mockMvc.perform(post("/users/{userId}", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is(DATA_CONFLICT)))
                .andExpect(jsonPath("$.error", hasItem(USERNAME_ALREADY_EXISTS)));
    }

    @WithMockUser(username="admin", roles = {"ADMIN"})
    @Test
    void test_isConflict_whenEmailUsed() throws Exception {

        UserRoleEntityBindingModel editedRole = this.setUp.createRoleToEdit(UserRole.ROLE_USER);

        UserEditBindingModel userToEdit = this.setUp
                .createUserToEdit("editedUsername", ADMIN_EMAIL, List.of(editedRole));

        String inputJson = super.mapToJson(userToEdit);

        this.mockMvc.perform(post("/users/{userId}", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is(DATA_CONFLICT)))
                .andExpect(jsonPath("$.error", hasItem(EMAIL_ALREADY_EXISTS)));
    }

    @WithMockUser(username="user")
    @Test
    void test_delete_isForbidden() throws Exception {

        this.mockMvc.perform(delete("/users/{userId}", USER_ID))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="admin", roles = {"ADMIN"})
    @Test
    void test_delete_isOk() throws Exception {

        this.mockMvc.perform(delete("/users/{userId}", USER_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username="admin", roles = {"ADMIN"})
    @Test
    void test_delete_isNotFound() throws Exception {

        this.mockMvc.perform(delete("/users/{userId}", "999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(NOT_FOUND)))
                .andExpect(jsonPath("$.error", hasItem(USER_NOT_FOUND)));
    }
}