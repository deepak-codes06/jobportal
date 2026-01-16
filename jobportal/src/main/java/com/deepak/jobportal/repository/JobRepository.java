package com.deepak.jobportal.repository;

import com.deepak.jobportal.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    // Search jobs
    Page<Job> findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCaseOrSkillsContainingIgnoreCase(
            String title,
            String location,
            String skills,
            Pageable pageable
            );
}
