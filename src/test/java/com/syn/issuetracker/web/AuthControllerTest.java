package com.syn.issuetracker.web;

import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.entity.UserRoleEntity;
import com.syn.issuetracker.model.enums.UserRole;
import com.syn.issuetracker.model.payload.request.LoginRequest;
import com.syn.issuetracker.model.payload.request.SignUpRequest;
import com.syn.issuetracker.repository.UserRepository;
import com.syn.issuetracker.repository.UserRoleRepository;
import com.syn.issuetracker.web.service.SetUpService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.syn.issuetracker.common.ExceptionErrorMessages.*;
import static com.syn.issuetracker.common.ValidationErrorMessages.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest extends JsonMapper {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SetUpService setUp;

    private String USER_EMAIL;
    private String USERNAME;
    private String USER_ID;

    @BeforeEach
    public void init() {
        this.tearDown();

        UserRoleEntity roleUser = this.setUp.createUserRole(UserRole.ROLE_USER);

        UserEntity user = this.setUp
                .createUser(List.of(roleUser), "user", "user@mail.com", "123");

        USER_EMAIL = user.getEmail();
        USERNAME = user.getUsername();
        USER_ID = user.getId();
    }

    @AfterEach
    public void tearDown() {
        this.setUp.tearDown();
    }

    @Test
    void test_signUp_isOk() throws Exception {

        SignUpRequest signUpRequest = this.setUp
                .createSignUpRequest("newUser", "newUser@mail.com", "123");

        String inputJson = super.mapToJson(signUpRequest);

        this.mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", CoreMatchers.not(emptyOrNullString())))
                .andExpect(jsonPath("$.id", CoreMatchers.not(emptyOrNullString())))
                .andExpect(jsonPath("$.username", is("newUser")));
    }

    @Test
    void test_signUp_isUnprocessable_whenInvalidInput() throws Exception {

        SignUpRequest signUpRequest = this.setUp
                .createSignUpRequest("", "email", "");

        String inputJson = super.mapToJson(signUpRequest);

        this.mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.description", is(VALIDATION_FAILURE)))
                .andExpect(jsonPath("$.error", hasSize(5)))
                .andExpect(jsonPath("$.error", hasItem(USERNAME_LENGTH)))
                .andExpect(jsonPath("$.error", hasItem(USERNAME_BLANK)))
                .andExpect(jsonPath("$.error", hasItem(PASSWORD_LENGTH)))
                .andExpect(jsonPath("$.error", hasItem(PASSWORD_BLANK)))
                .andExpect(jsonPath("$.error", hasItem(EMAIL_NOT_VALID)));
    }

    @Test
    void test_signUp_isConflict_whenEmailExists() throws Exception {

        SignUpRequest signUpRequest = this.setUp
                .createSignUpRequest("newUser", USER_EMAIL, "123");

        String inputJson = super.mapToJson(signUpRequest);

        this.mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is(DATA_CONFLICT)))
                .andExpect(jsonPath("$.error", hasSize(1)))
                .andExpect(jsonPath("$.error", hasItem(EMAIL_ALREADY_EXISTS)));
    }

    @Test
    void test_signUp_isConflict_whenUsernameExists() throws Exception {

        SignUpRequest signUpRequest = this.setUp
                .createSignUpRequest(USERNAME, "new@mail.com", "123");

        String inputJson = super.mapToJson(signUpRequest);

        this.mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is(DATA_CONFLICT)))
                .andExpect(jsonPath("$.error", hasSize(1)))
                .andExpect(jsonPath("$.error", hasItem(USERNAME_ALREADY_EXISTS)));
    }

    @Test
    void test_signIn_isOk() throws Exception {

        LoginRequest loginRequest = this.setUp.createLoginRequest(USERNAME, "123");

        String inputJson = super.mapToJson(loginRequest);

        this.mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", CoreMatchers.not(emptyOrNullString())))
                .andExpect(jsonPath("$.id", is(USER_ID)))
                .andExpect(jsonPath("$.username", is(USERNAME)));
    }

    @Test
    void test_signIn_isUnprocessable_whenInvalidInput() throws Exception {

        LoginRequest loginRequest = this.setUp
                .createLoginRequest("", "");

        String inputJson = super.mapToJson(loginRequest);

        this.mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.description", is(VALIDATION_FAILURE)))
                .andExpect(jsonPath("$.error", hasSize(4)))
                .andExpect(jsonPath("$.error", hasItem(USERNAME_LENGTH)))
                .andExpect(jsonPath("$.error", hasItem(USERNAME_BLANK)))
                .andExpect(jsonPath("$.error", hasItem(PASSWORD_LENGTH)))
                .andExpect(jsonPath("$.error", hasItem(PASSWORD_BLANK)));
    }

    @Test
    void test_signIn_isUnprocessable_whenInvalidUser() throws Exception {

        LoginRequest loginRequest = this.setUp
                .createLoginRequest("invalid", "invalid");

        String inputJson = super.mapToJson(loginRequest);

        this.mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", is(VALIDATION_FAILURE)))
                .andExpect(jsonPath("$.error", hasSize(1)))
                .andExpect(jsonPath("$.error", hasItem(AUTH_INVALID)));
    }
}