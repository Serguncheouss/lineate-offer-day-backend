package com.example.offerdaysongs.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Entity
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    @JoinColumn(name = "recording_id", nullable = false)
    Recording recording;
    ZonedDateTime startTime;
    ZonedDateTime endTime;
    BigDecimal cost;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    Company company;
}
