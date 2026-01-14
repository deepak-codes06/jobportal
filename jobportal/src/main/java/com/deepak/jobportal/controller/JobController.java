package com.deepak.jobportal.controller;

import com.deepak.jobportal.entity.Job;
import com.deepak.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/post")
    public Job postJob(@RequestBody Job job){
        return jobService.postJob(job);
    }

    @GetMapping("/all")
    public List<Job> getAllJobs(){
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public Job getJob (@PathVariable Long id){
        return jobService.getJobById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteJob(@PathVariable Long id){
        jobService.deleteJob(id);
        return "Job Deleted Successfully";
    }


}
