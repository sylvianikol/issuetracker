package com.syn.issuetracker.model.service;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static com.syn.issuetracker.common.ValidationErrorMessages.*;
import static com.syn.issuetracker.common.ValidationErrorMessages.DATE_FUTURE;

public class TaskServiceModel extends BaseServiceModel {

    private String title;
    private String description;
    private LocalDateTime createdOn;
    private String status;
    private UserServiceModel developer;
    private String priority;

    public TaskServiceModel() {
    }

    @NotBlank(message = TITLE_BLANK)
    @Size(min = 2, message = TITLE_LENGTH)
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

    @NotNull(message = DATE_NULL)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = DATE_FUTURE)
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

    public UserServiceModel getDeveloper() {
        return developer;
    }

    public void setDeveloper(UserServiceModel developer) {
        this.developer = developer;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}
