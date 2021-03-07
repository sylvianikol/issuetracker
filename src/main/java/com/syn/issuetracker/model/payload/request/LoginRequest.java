package com.syn.issuetracker.model.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.syn.issuetracker.common.ValidationErrorMessages.*;

public class LoginRequest {

    private String username;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @NotBlank(message = USERNAME_BLANK)
    @Size(min = 3, max = 30, message = USERNAME_LENGTH)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank(message = PASSWORD_BLANK)
    @Size(min = 3, message = PASSWORD_LENGTH)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
