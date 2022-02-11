package com.example.offerdaysongs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class LicenseDto {
    long id;
    RecordingDto recording;
    ZonedDateTime startTime;
    ZonedDateTime endTime;
    BigDecimal cost;
    CompanyDto company;
}
