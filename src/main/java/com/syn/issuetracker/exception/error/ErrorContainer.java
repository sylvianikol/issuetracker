package com.syn.issuetracker.exception.error;

import java.util.*;

public class ErrorContainer {

    private List<String> errors;

    public ErrorContainer() {
        this.errors = new ArrayList<>();
    }

    public ErrorContainer(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String>errors) {
        this.errors = errors;
    }

    public ErrorContainer addError(String error) {
        this.errors.add(error);
        return this;
    }
}
