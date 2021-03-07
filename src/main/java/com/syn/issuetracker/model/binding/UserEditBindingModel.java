package com.syn.issuetracker.model.binding;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static com.syn.issuetracker.common.ValidationErrorMessages.*;

public class UserEditBindingModel {

    private String username;
    private String email;
    private List<UserRoleEntityBindingModel> authorities;

    public UserEditBindingModel() {
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

    public List<UserRoleEntityBindingModel> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<UserRoleEntityBindingModel> authorities) {
        this.authorities = authorities;
    }
}
