package com.syn.issuetracker.repository;

import com.syn.issuetracker.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {

    List<Task> findAllByDeveloper_IdOrderByCreatedOnDescCompleted(String userId);

    List<Task> findAllByOrderByCreatedOnDescCompleted();

    Page<Task> findAllByTitle(String title, Pageable pageable);

    Optional<Task> findByTitle(String title);

    List<Task> findByCompleted(boolean completed);

    List<Task> findByDeveloper_IdAndTitleContainingOrderByCreatedOnDesc(String userId, String title);
}
