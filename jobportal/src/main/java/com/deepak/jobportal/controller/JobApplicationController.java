package com.deepak.jobportal.controller;

import com.deepak.jobportal.entity.JobApplication;
import com.deepak.jobportal.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
public class JobApplicationController {
    @Autowired
    private JobApplicationService jobApplicationService;

    @PostMapping("/apply")
    public ResponseEntity applyJob(
            @RequestParam Long jobId,
            Authentication authentication

    ){
        String email = authentication.getName();
        jobApplicationService.apply(email, jobId);
        return ResponseEntity.ok("Applied successfully");
    }
}
