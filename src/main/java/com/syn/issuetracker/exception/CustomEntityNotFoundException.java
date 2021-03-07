package com.syn.issuetracker.exception;

import java.util.ArrayList;
import java.util.List;

public class CustomEntityNotFoundException extends RuntimeException {

    private List<String> errors;

    public CustomEntityNotFoundException(String error) {
        super(error);
        this.errors = new ArrayList<>();
    }

    public CustomEntityNotFoundException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
