package com.syn.issuetracker.model.binding;

import com.syn.issuetracker.model.enums.UserRole;

public class UserRoleEntityBindingModel {

    private UserRole role;

    public UserRoleEntityBindingModel() {
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
