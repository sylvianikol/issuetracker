package com.syn.issuetracker.repository;

import com.syn.issuetracker.model.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, String> {
}
