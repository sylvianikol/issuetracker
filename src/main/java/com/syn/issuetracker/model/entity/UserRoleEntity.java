package com.syn.issuetracker.model.entity;

import com.syn.issuetracker.enums.UserRole;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class UserRoleEntity extends BaseEntity {

    private UserRole role;

    public UserRoleEntity() {
    }

    @Enumerated(EnumType.STRING)
    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
