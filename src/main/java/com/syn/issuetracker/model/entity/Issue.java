package com.syn.issuetracker.model.entity;

import com.syn.issuetracker.enums.IssueType;
import com.syn.issuetracker.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
public abstract class Issue extends BaseEntity {

    private String title;
    private String description;
    private LocalDate creationDate;
    private IssueType issueType;
    private Status status;
    private Developer developer;

    public Issue() {
    }

    @Column(nullable = false)
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

    @Column(name = "creation_date", nullable = false)
    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Enumerated(value = EnumType.STRING)
    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    @Enumerated(value = EnumType.STRING)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne
    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }
}
