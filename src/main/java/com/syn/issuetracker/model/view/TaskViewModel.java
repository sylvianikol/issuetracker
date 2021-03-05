package com.syn.issuetracker.model.view;

import java.time.LocalDateTime;

public class TaskViewModel {

    private String id;
    private String title;
    private String description;
    private LocalDateTime createdOn;
    private String status;
    private UserViewModel developer;
    private String priority;

    public TaskViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserViewModel getDeveloper() {
        return developer;
    }

    public void setDeveloper(UserViewModel developer) {
        this.developer = developer;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
