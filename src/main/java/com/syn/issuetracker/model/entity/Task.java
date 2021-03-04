package com.syn.issuetracker.model.entity;

import com.syn.issuetracker.enums.Priority;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {

    private String title;
    private String description;
    private LocalDateTime createdOn;
    private boolean completed;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Enumerated(value = EnumType.STRING)
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @ManyToOne
    public UserEntity getDeveloper() {
        return this.developer;
    }

    public void setDeveloper(UserEntity developer) {
        this.developer = developer;
    }
}
