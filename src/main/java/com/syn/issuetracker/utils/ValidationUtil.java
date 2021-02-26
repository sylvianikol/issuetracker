package com.syn.issuetracker.utils;

import com.syn.issuetracker.exception.error.ErrorContainer;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidationUtil {

    <T> boolean isValid(T entity);

    <T> Set<ConstraintViolation<T>> violations(T entity);

    <T> ErrorContainer getViolations(T entity);
}
