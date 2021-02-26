package com.syn.issuetracker.repository;

import com.syn.issuetracker.model.entity.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BugRepository extends JpaRepository<Bug, String> {
}
