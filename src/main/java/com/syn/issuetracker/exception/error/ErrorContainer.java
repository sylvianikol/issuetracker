package com.syn.issuetracker.exception.error;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ErrorContainer {

    private Map<String, Set<String>> errors;

    public ErrorContainer() {
        this.errors = new HashMap<>();
    }

    public ErrorContainer(Map<String, Set<String>> errors) {
        this.errors = errors;
    }

    public Map<String, Set<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Set<String>> errors) {
        this.errors = errors;
    }

}
