package com.syn.issuetracker.exception;

import java.util.ArrayList;
import java.util.List;

public class UnprocessableEntityException extends RuntimeException {

    private List<String> errors;

    public UnprocessableEntityException(String error) {
        super(error);
        this.errors = new ArrayList<>();
    }

    public UnprocessableEntityException(String error, List<String> errors) {
        super(error);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
