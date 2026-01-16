package com.deepak.jobportal.service;

import com.deepak.jobportal.entity.Job;
import com.deepak.jobportal.entity.User;
import com.deepak.jobportal.exception.JobNotFoundException;
import com.deepak.jobportal.exception.UserNotFoundException;
import com.deepak.jobportal.repository.JobRepository;
import com.deepak.jobportal.repository.UserRepository;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.security.PublicKey;
import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    public Job createJob(Job job){
        User currentUser = getCurrentUser();
        job.setRecruiter(currentUser);
        return jobRepository.save(job);
    }

    public Job updateJob(Long jobId, Job updatedJob){
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        User currentUser = getCurrentUser();

        if(!job.getRecruiter().getId().equals(currentUser.getId())){
            throw new RuntimeException("You are not allowed to update this job");
        }

        job.setTitle(updatedJob.getTitle());
        job.setDescription(updatedJob.getDescription());
        job.setLocation(updatedJob.getLocation());
        job.setSalary(updatedJob.getSalary());

        return jobRepository.save(job);
    }

    public List<Job> getAllJobs(){
        return jobRepository.findAll();
    }


    public void deleteJob(Long jobId){
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        User currentUser = getCurrentUser();

        if(!job.getRecruiter().getId().equals(currentUser.getId())){
            throw new RuntimeException("You are not allowed to delete this job");
        }

        jobRepository.delete(job);
    }

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }


    public Page<Job> searchJobs(String keyword , Pageable pageable){
        return jobRepository.findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCaseOrSkillsContainingIgnoreCase(
                keyword,
                keyword,
                keyword,
                pageable
        );
    }

}
