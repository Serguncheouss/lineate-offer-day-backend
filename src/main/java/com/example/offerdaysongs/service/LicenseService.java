package com.example.offerdaysongs.service;

import com.example.offerdaysongs.dto.requests.CreateLicenseRequest;
import com.example.offerdaysongs.dto.requests.UpdateLicenseRequest;
import com.example.offerdaysongs.model.Recording;
import com.example.offerdaysongs.model.Company;
import com.example.offerdaysongs.model.License;
import com.example.offerdaysongs.repository.CompanyRepository;
import com.example.offerdaysongs.repository.RecordingRepository;
import com.example.offerdaysongs.repository.LicenseRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LicenseService {
    private final LicenseRepository licenseRepository;
    private final RecordingRepository recordingRepository;
    private final CompanyRepository companyRepository;

    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";
    private static final String COMPANY = "company";
    private static final String RECORDING = "recording";

    public LicenseService(LicenseRepository licenseRepository,
                          RecordingRepository recordingRepository,
                          CompanyRepository companyRepository) {
        this.licenseRepository = licenseRepository;
        this.recordingRepository = recordingRepository;
        this.companyRepository = companyRepository;
    }

    public List<License> getAll(ZonedDateTime startTime, ZonedDateTime endTime, Long companyId) {
        var specs = new ArrayList<Specification<License>>();
        specs.add(licenseInIntervalGreaterThanOrEqualTo(startTime));
        specs.add(licenseInIntervalLessThanOrEqualTo(endTime));
        specs.add(licenseHasCompany(companyId));

        return licenseRepository.findAll(
                (root, query, builder) -> builder.and(specs.stream()
                        .map(spec -> spec.toPredicate(root, query, builder))
                        .toArray(Predicate[]::new)));
    }

    public List<License> getByRecording(Long recordingId) {
        Recording recording = (recordingId != null) ?
                recordingRepository.findById(recordingId).orElse(null) :
                null;
        return licenseRepository.findByRecording(recording);
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

    @Nullable
    @Transactional
    public License update(long id, UpdateLicenseRequest request) {
        var license = licenseRepository.findById(id).orElse(null);
        if (license == null) {
            return null;
        }

        var requestRecording = request.getRecording();
        var recording = (requestRecording != null) ? getRecording(requestRecording) : null;
        if (recording != null && !license.getRecording().getId().equals(recording.getId())) {
            license.setRecording(getRecording(recording));
        }

        var startTime = request.getStartTime();
        if (startTime != null && !license.getStartTime().isEqual(startTime)) {
            license.setStartTime(startTime);
        }

        var endTime = request.getEndTime();
        if (endTime != null && !license.getEndTime().isEqual(endTime)) {
            license.setEndTime(endTime);
        }

        var cost = request.getCost();
        if (cost != null && !license.getCost().equals(cost)) {
            license.setCost(cost);
        }

        var requestCompany = request.getCompany();
        var company = (requestCompany != null) ? getCompany(requestCompany) : null;
        if (company != null && license.getCompany().getId() != company.getId()) {
            license.setCompany(getCompany(company));
        }

        return license;
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
        return (company.getId() != 0) ?
                companyRepository.findById(company.getId()).orElse(null) :
                null;
    }

    private Specification<License> licenseInIntervalGreaterThanOrEqualTo(@Nullable ZonedDateTime startTime) {
        return (root, query, builder) -> (startTime != null) ?
                builder.or(
                        builder.greaterThanOrEqualTo(root.get(START_TIME), startTime),
                        builder.greaterThanOrEqualTo(root.get(END_TIME), startTime)) :
                builder.conjunction();
    }

    private Specification<License> licenseInIntervalLessThanOrEqualTo(@Nullable ZonedDateTime endTime) {
        return (root, query, builder) -> (endTime != null) ?
                builder.or(
                        builder.lessThanOrEqualTo(root.get(END_TIME), endTime),
                        builder.lessThanOrEqualTo(root.get(START_TIME), endTime)) :
                builder.conjunction();
    }

    private Specification<License> licenseHasCompany(@Nullable Long companyId) {
        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            return (root, query, builder) -> (company != null) ?
                    builder.equal(root.get(COMPANY), company) :
                    builder.disjunction();
        } else {
            return (root, query, builder) -> builder.conjunction();
        }
    }
}
