package com.syn.issuetracker.utils;

import com.syn.issuetracker.exception.error.ErrorContainer;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

public class ValidationUtilImpl implements ValidationUtil {

    private final Validator validator;

    public ValidationUtilImpl() {
        this.validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Override
    public <T> boolean isValid(T entity) {
        return this.validator.validate(entity).isEmpty();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> violations(T entity) {
        return this.validator.validate(entity);
    }

//    @Override
//    public <T> ErrorContainer getViolations(T entity) {
//        Set<ConstraintViolation<T>> violations = this.violations(entity);
//
//        ErrorContainer errorContainer = new ErrorContainer();
//
//        for (ConstraintViolation<T> violation : violations) {
//            String key = violation.getPropertyPath().toString();
//            String value = violation.getMessage();
//
//            errorContainer.getErrors().putIfAbsent(key, new HashSet<>());
//            errorContainer.getErrors().get(key).add(value);
//        }
//
//        return errorContainer;
//    }
}
