package com.syn.issuetracker.model.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.syn.issuetracker.common.ValidationErrorMessages.*;
import static com.syn.issuetracker.common.ValidationErrorMessages.PASSWORD_LENGTH;

public class SignUpRequest {

    private String username;
    private String email;
    private String password;

    public SignUpRequest() {
    }

    @NotBlank(message = USERNAME_BLANK)
    @Size(min = 3, max = 30, message = USERNAME_LENGTH)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank(message = EMAIL_BLANK)
    @Email(message = EMAIL_NOT_VALID)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank(message = PASSWORD_BLANK)
    @Size(min = 6, message = PASSWORD_LENGTH)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
