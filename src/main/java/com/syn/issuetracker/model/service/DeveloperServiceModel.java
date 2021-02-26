package com.syn.issuetracker.model.service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class DeveloperServiceModel extends BaseServiceModel {

    private String name;

    public DeveloperServiceModel() {
    }

    @NotBlank(message = "Name should not be empty!")
    @Size(min = 2, max = 50, message = "Name length should be between 2 and 50 characters.")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
