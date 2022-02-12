package com.example.offerdaysongs.dto.requests;

import com.example.offerdaysongs.model.Company;
import com.example.offerdaysongs.model.Recording;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class UpdateLicenseRequest {
    private Recording recording;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private BigDecimal cost;
    private Company company;
}
