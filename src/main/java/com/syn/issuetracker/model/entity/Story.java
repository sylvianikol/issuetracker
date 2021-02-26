package com.syn.issuetracker.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "stories")
public class Story extends Issue {

    private Integer point;
    private Integer assignedWeek;

    public Story() {
    }

    @Column
    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    @Column
    public Integer getAssignedWeek() {
        return assignedWeek;
    }

    public void setAssignedWeek(Integer assignedWeek) {
        this.assignedWeek = assignedWeek;
    }
}
