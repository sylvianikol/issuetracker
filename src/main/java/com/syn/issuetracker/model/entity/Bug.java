package com.syn.issuetracker.model.entity;

import com.syn.issuetracker.enums.Priority;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "bugs")
public class Bug extends Issue {

    private Priority priority;

    public Bug() {
    }

    @Enumerated(value = EnumType.STRING)
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
