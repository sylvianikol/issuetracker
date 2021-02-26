package com.syn.issuetracker.repository;

import com.syn.issuetracker.model.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, String> {

    Optional<Developer> findByName(String name);
}
