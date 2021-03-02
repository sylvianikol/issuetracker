package com.syn.issuetracker.model.service;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserServiceModel extends BaseServiceModel {

    private String username;
    private String email;
    private String token;

    public UserServiceModel() {
    }

    @NotBlank(message = "Name should not be empty!")
    @Size(min = 2, max = 50, message = "Name length should be between 2 and 50 characters.")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
