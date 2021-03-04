package com.syn.issuetracker.model.view;

import java.util.List;

public class UserViewModel {

    private String id;
    private String username;
    private String email;
    private List<UserRoleViewModel> authorities;

    public UserViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<UserRoleViewModel> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<UserRoleViewModel> authorities) {
        this.authorities = authorities;
    }
}
