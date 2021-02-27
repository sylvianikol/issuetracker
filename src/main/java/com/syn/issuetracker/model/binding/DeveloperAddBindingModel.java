package com.syn.issuetracker.model.binding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.syn.issuetracker.common.ValidationErrorMessages.NAME_BLANK;
import static com.syn.issuetracker.common.ValidationErrorMessages.NAME_LENGTH;

public class DeveloperAddBindingModel {

    private String name;

    public DeveloperAddBindingModel() {
    }

    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, max = 50, message = NAME_LENGTH)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
