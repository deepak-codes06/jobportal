package com.deepak.jobportal.controller;

import com.deepak.jobportal.entity.JobApplication;
import com.deepak.jobportal.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    // ✅ APPLY JOB (JOBSEEKER)
    @PreAuthorize("hasRole('JOBSEEKER')")
    @PostMapping("/apply/{jobId}")
    public ResponseEntity<String> applyJob(@PathVariable Long jobId) {
        jobApplicationService.apply(jobId);
        return ResponseEntity.ok("Applied successfully");
    }

    // ✅ VIEW APPLICANTS (RECRUITER)
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/job/{jobId}/applicants")
    public List<JobApplication> getApplicants(@PathVariable Long jobId) {
        return jobApplicationService.getApplicants(jobId);
    }

    // ✅ UPLOAD RESUME (JOBSEEKER)
    @PreAuthorize("hasAuthority('ROLE_JOBSEEKER')")
    @PostMapping("/{applicationId}/upload-resume")
    public ResponseEntity<?> uploadResume(
            @PathVariable Long applicationId,
            @RequestParam("file") MultipartFile file) {

        // Just for Understanding
        System.out.println("Authorities => " +
                SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        return ResponseEntity.ok(jobApplicationService.uploadResume(applicationId, file));
    }

    // ✅ DOWNLOAD RESUME (RECRUITER)
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/{applicationId}/download-resume")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long applicationId) throws Exception {

        Resource resource = jobApplicationService.downloadResume(applicationId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"resume.pdf\"")
                .body(resource);
    }
}
