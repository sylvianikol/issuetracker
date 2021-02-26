package com.syn.issuetracker.model.service;

public abstract class BaseServiceModel {

    private String id;

    public BaseServiceModel() {
    }

    public BaseServiceModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
