package com.example.offerdaysongs.service;

import com.example.offerdaysongs.dto.requests.CreateLicenseRequest;
import com.example.offerdaysongs.model.Recording;
import com.example.offerdaysongs.model.Company;
import com.example.offerdaysongs.model.License;
import com.example.offerdaysongs.repository.CompanyRepository;
import com.example.offerdaysongs.repository.RecordingRepository;
import com.example.offerdaysongs.repository.LicenseRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class LicenseService {
    private final LicenseRepository licenseRepository;
    private final RecordingRepository recordingRepository;
    private final CompanyRepository companyRepository;

    public LicenseService(LicenseRepository licenseRepository,
                          RecordingRepository recordingRepository,
                          CompanyRepository companyRepository) {
        this.licenseRepository = licenseRepository;
        this.recordingRepository = recordingRepository;
        this.companyRepository = companyRepository;
    }

    public List<License> getAll() {
        return licenseRepository.findAll();
    }

    @Nullable
    @Transactional
    public License create(CreateLicenseRequest request) {
        var recording = getRecording(request.getRecording());
        if (recording == null) {
            return null;
        }
        var company = getCompany(request.getCompany());
        if (company == null) {
            return null;
        }

        License license = new License();
        license.setRecording(recording);
        license.setStartTime(request.getStartTime());
        license.setEndTime(request.getEndTime());
        license.setCost(request.getCost());
        license.setCompany(company);

        return licenseRepository.save(license);
    }

    @Transactional
    public void delete(long id) {
        licenseRepository.deleteById(id);
    }

    @Nullable
    private Recording getRecording(Recording recording) {
        return (recording.getId() != null) ?
                recordingRepository.findById(recording.getId()).orElse(null) :
                null;
    }

    @Nullable
    private Company getCompany(Company company) {
        return companyRepository.findById(company.getId()).orElse(null);
    }
}
