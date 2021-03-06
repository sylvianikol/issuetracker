package com.syn.issuetracker.model.binding;

import java.util.List;

public class UserEditBindingModel {

    private String username;
    private String email;
    private List<UserRoleEntityBindingModel> authorities;

    public UserEditBindingModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
