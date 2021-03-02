package com.syn.issuetracker.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    private String email;
    private String username;
    private String password;
    private List<UserRoleEntity> authorities = new ArrayList<>();

    public UserEntity() {
    }

    @Column(nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    @Column(nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String name) {
        this.email = name;
    }

    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "role_id"))
    public List<UserRoleEntity> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<UserRoleEntity> authorities) {
        this.authorities = authorities;
    }
}
