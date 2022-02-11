package com.example.offerdaysongs.controller;

import com.example.offerdaysongs.dto.CompanyDto;
import com.example.offerdaysongs.dto.LicenseDto;
import com.example.offerdaysongs.dto.RecordingDto;
import com.example.offerdaysongs.dto.SingerDto;
import com.example.offerdaysongs.dto.requests.CreateLicenseRequest;
import com.example.offerdaysongs.model.License;
import com.example.offerdaysongs.service.LicenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/licenses")
public class LicenseController {
    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping("/")
    public ResponseEntity<LicenseDto> create(@Valid @RequestBody CreateLicenseRequest request) {
        License license = licenseService.create(request);
        return (license != null) ?
                new ResponseEntity<>(convertToDto(license), HttpStatus.OK) :
                ResponseEntity.badRequest().build();
    }

    private LicenseDto convertToDto(License license) {
        var recording = license.getRecording();
        var singer = recording.getSinger();
        var company = license.getCompany();
        return new LicenseDto(license.getId(),
                new RecordingDto(recording.getId(),
                        recording.getTitle(),
                        recording.getVersion(),
                        recording.getReleaseTime(),
                        new SingerDto(singer.getId(), singer.getName())),
                license.getStartTime(),
                license.getEndTime(),
                license.getCost(),
                new CompanyDto(company.getId(), company.getName()));
    }
}