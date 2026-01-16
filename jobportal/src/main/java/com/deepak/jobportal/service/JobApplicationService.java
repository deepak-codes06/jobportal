package com.deepak.jobportal.service;

import com.deepak.jobportal.entity.Job;
import com.deepak.jobportal.entity.JobApplication;
import com.deepak.jobportal.entity.User;
import com.deepak.jobportal.exception.AlreadyAppliedException;
import com.deepak.jobportal.exception.JobNotFoundException;
import com.deepak.jobportal.exception.UserNotFoundException;
import com.deepak.jobportal.repository.JobApplicationRepository;
import com.deepak.jobportal.repository.JobRepository;
import com.deepak.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@Service
public class JobApplicationService {


    @Autowired
    private JobApplicationRepository jobApplicationRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JobRepository jobRepo;

    public JobApplication apply(Long jobId){
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("job not found"));


        if(jobApplicationRepo.existsByUserIdAndJobId(user.getId(), job.getId())){
            throw new AlreadyAppliedException("You already applied for this job");
        }

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);
        application.setStatus("APPLIED");

        return jobApplicationRepo.save(application);

    }


    public List<JobApplication> getApplicants(Long jobId){
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        // get recruiter from JWT
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // ownership check

        if(!job.getRecruiter().getId().equals(currentUser.getId())){
            throw new RuntimeException("You are not allowed to view applicants");
        }

        return jobApplicationRepo.findByJob(job);
    }


    public JobApplication  uploadResume(Long applicationId , MultipartFile file){

        String fileName = file.getOriginalFilename();

        // this helps to validate the file
        if(file.isEmpty() || fileName == null || !fileName.toLowerCase().endsWith(".pdf")){
            throw new RuntimeException("Only pdf files are allowed");
        }

        // this helps to getting the application
        JobApplication application = jobApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));


        // Ownership check (jobseeker uploading own resume)
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(!application.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("You are not allowed to upload this resume");
        }

        // to save the file
        try {
            String uploadDir = "uploads/resumes/";
            Files.createDirectories(Paths.get(uploadDir));

            String saveFileName = "resume_app_" + applicationId + ".pdf";
            Path filePath = Paths.get(uploadDir + saveFileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // save path in DB
            application.setResumePath(filePath.toString());
            return jobApplicationRepo.save(application);
        }catch (Exception e){
            throw new RuntimeException("Resume upload failed");
        }
    }



    public Resource downloadResume(Long applicationId) throws Exception{
        JobApplication application = jobApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // recruiter ownership check
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        if(!application.getJob().getRecruiter().getId().equals(recruiter.getId())){
                    throw new RuntimeException("Not allowed to download resume");
                }

                Path path = Paths.get(application.getResumePath());
                return new UrlResource(path.toUri());
    }
}
