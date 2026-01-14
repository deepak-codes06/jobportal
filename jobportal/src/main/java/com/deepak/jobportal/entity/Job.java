package com.deepak.jobportal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String location;
    private String company;
    private String skillsRequired;

    @Column(nullable = false)
    private LocalDate postedDate;
    @PrePersist
    public void setPostedDate(){
        this.postedDate = LocalDate.now();
    }
}
