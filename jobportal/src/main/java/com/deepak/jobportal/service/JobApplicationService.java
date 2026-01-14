package com.deepak.jobportal.service;

import com.deepak.jobportal.entity.Job;
import com.deepak.jobportal.entity.JobApplication;
import com.deepak.jobportal.entity.User;
import com.deepak.jobportal.repository.JobApplicationRepository;
import com.deepak.jobportal.repository.JobRepository;
import com.deepak.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class JobApplicationService {


    @Autowired
    private JobApplicationRepository jobApplicationRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JobRepository jobRepo;

    public JobApplication apply(String email, Long jobId){
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if(jobApplicationRepo.existsByUserIdAndJobId(user.getId(), job.getId())){
            throw new RuntimeException("You Already applied for this job :) ");
        }

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);
        application.setStatus("APPLIED");

        return jobApplicationRepo.save(application);
    }
}
