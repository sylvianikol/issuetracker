package com.syn.issuetracker.specification;

import com.syn.issuetracker.model.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification implements Specification<UserEntity> {

    private final String username;

    public UserSpecification(String username) {
        this.username = username;
    }

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        if (username != null) {
            predicate.getExpressions().add(cb.like(root.get("username"), "%" + username + "%"));
        }

        return predicate;
    }
}
