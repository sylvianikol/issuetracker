package com.syn.issuetracker.exception;

import java.util.ArrayList;
import java.util.List;

public class DataConflictException extends RuntimeException {

    private List<String> errors;

    public DataConflictException(String error) {
        super(error);
        this.errors = new ArrayList<>();
    }

    public DataConflictException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
