package com.deepak.jobportal.repository;

import com.deepak.jobportal.entity.Job;
import com.deepak.jobportal.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
    List<JobApplication> findByJob(Job job);
}
