package com.syn.issuetracker.model.binding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.syn.issuetracker.common.ValidationErrorMessages.TITLE_BLANK;
import static com.syn.issuetracker.common.ValidationErrorMessages.TITLE_LENGTH;

public class TaskEditBindingModel {

    private String title;
    private String description;
    private String priority;
    private String userId;
    private boolean completed;

    public TaskEditBindingModel() {
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
