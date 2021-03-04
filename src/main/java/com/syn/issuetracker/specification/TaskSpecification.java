package com.syn.issuetracker.specification;

import com.syn.issuetracker.model.entity.Task;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TaskSpecification implements Specification<Task> {

    private final String userId;
    private final String title;

    public TaskSpecification(String userId, String title) {
        this.userId = userId;
        this.title = title;
    }

    @Override
    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        Predicate predicate = cb.conjunction();

        if (userId != null) {
            predicate.getExpressions().add(cb.equal(root.get("developer").get("id"), userId));
        }

        if (title != null) {
            predicate.getExpressions().add(cb.equal(root.get("title").get("title"), title));
        }

        return predicate;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }
}
