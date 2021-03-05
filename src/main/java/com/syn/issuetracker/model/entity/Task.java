package com.syn.issuetracker.model.entity;

import com.syn.issuetracker.enums.Priority;
import com.syn.issuetracker.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {

    private String title;
    private String description;
    private LocalDateTime createdOn;
    private Status status;
    private Priority priority;
    private UserEntity developer;

    public Task() {
    }

    @Column(nullable = false, unique = true)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "created_on", nullable = false)
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Enumerated(EnumType.ORDINAL)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Enumerated(value = EnumType.STRING)
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public UserEntity getDeveloper() {
        return this.developer;
    }

    public void setDeveloper(UserEntity developer) {
        this.developer = developer;
    }
}
