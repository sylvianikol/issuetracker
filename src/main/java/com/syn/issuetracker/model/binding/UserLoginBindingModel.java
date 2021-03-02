package com.syn.issuetracker.model.binding;

import com.syn.issuetracker.common.ValidationErrorMessages;

import javax.validation.constraints.Email;

public class UserLoginBindingModel {

    private String email;
    private String password;

    public UserLoginBindingModel() {
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
