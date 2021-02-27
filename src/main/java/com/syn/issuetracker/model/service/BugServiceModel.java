package com.syn.issuetracker.model.service;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static com.syn.issuetracker.common.ValidationErrorMessages.*;
import static com.syn.issuetracker.common.ValidationErrorMessages.DATE_PAST;

public class BugServiceModel extends BaseServiceModel {

    private String title;
    private String description;
    private LocalDate creationDate;
    private String issueType;
    private String status;
    private String developerId;
    private String priority;

    public BugServiceModel() {
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
    @FutureOrPresent(message = DATE_PAST)
    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @NotBlank(message = ISSUE_TYPE_BLANK)
    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    @NotBlank(message = STATUS_BLANK)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NotBlank(message = DEVELOPER_ID_BLANK)
    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    @NotBlank(message = PRIORITY_BLANK)
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
