package com.syn.issuetracker.payload.request;

import javax.validation.constraints.Email;

public class SignUpRequest {

    private String username;
    private String email;
    private String password;

    public SignUpRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Email(message = "Email is not valid")
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
