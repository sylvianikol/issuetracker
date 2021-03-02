package com.syn.issuetracker.repository;

import com.syn.issuetracker.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findAllByUserIdAndOrderByCreatedOnAsc(String userId);

    List<Task> findAllByAndOrderByCreatedOnAsc();

    List<Task> findAllByTitleAndOrderByCreatedOnAsc(String title);

    Optional<Task> findByTitle(String title);

    List<Task> findByCompleted(boolean completed);

    List<Task> findByUserIdAndTitleContaining(String userId, String title);
}
