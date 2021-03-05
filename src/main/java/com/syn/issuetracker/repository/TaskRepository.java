package com.syn.issuetracker.repository;

import com.syn.issuetracker.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {

    @Query("SELECT t FROM Task t " +
            "WHERE t.developer.id = :userId " +
            "ORDER BY t.createdOn DESC, t.status ")
    List<Task> getAllByUserId(@Param(value = "userId") String userId);

    Page<Task> findAllByTitle(String title, Pageable pageable);

    Optional<Task> findByTitle(String title);
}
