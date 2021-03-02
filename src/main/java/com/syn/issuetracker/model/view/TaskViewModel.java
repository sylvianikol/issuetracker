package com.syn.issuetracker.model.view;

import java.time.LocalDate;

public class TaskViewModel {

    private String id;
    private String title;
    private String description;
    private LocalDate createdOn;
    private boolean completed;
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

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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
