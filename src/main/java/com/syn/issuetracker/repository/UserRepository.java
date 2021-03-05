package com.syn.issuetracker.repository;

import com.syn.issuetracker.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u FROM UserEntity u " +
            "JOIN u.authorities roles " +
            "WHERE u.id = :userId AND roles.role = 'ROLE_ADMIN'")
    Optional<UserEntity> getIfAdmin(@Param(value = "userId") String userId);
}
