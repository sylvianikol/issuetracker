package com.syn.issuetracker.model.entity;

import com.syn.issuetracker.model.enums.UserRole;

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

    public UserRoleEntity(UserRole role) {
        this.role = role;
    }

    @Enumerated(value = EnumType.STRING)
    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
