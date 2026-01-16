package com.deepak.jobportal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, length = 2000)
    private String description;


    private String skills;

    private String experience; // Fresher , 1-3 Years

    private Double salary;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private LocalDateTime createdAt;

    // Recruiter is one who posted job
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
