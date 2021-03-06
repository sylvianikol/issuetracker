package com.syn.issuetracker.repository;

import com.syn.issuetracker.model.enums.UserRole;
import com.syn.issuetracker.model.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, String> {

    UserRoleEntity findByRole(UserRole role);
}
