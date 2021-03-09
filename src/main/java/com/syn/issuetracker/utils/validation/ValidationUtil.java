package com.syn.issuetracker.utils.validation;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

public interface ValidationUtil {

    <T> boolean isValid(T entity);

    <T> Set<ConstraintViolation<T>> violations(T entity);

    <T> List<String> getViolations(T entity);
}
