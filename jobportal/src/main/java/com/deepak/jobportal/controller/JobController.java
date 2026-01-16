package com.deepak.jobportal.controller;

import com.deepak.jobportal.entity.Job;
import com.deepak.jobportal.service.JobService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job){
        return ResponseEntity.ok(jobService.createJob(job));
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs(){
        return ResponseEntity.ok(jobService.getAllJobs());
    }

   @PreAuthorize("hasRole('RECRUITER')")
   @PutMapping("/{id}")
   public ResponseEntity<Job> updateJob(@PathVariable Long id,
                                        @RequestBody Job job){
        return ResponseEntity.ok(jobService.updateJob(id, job));
   }

   @PreAuthorize("hasRole('RECRUITER')")
   @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id){
        jobService.deleteJob(id);
        return ResponseEntity.ok("Job deleted successfully");
    }


    @GetMapping("/search")
    public Page<Job> searchJob(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ){
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy).descending()
        );

        return jobService.searchJobs(keyword, pageable);
    }


}
