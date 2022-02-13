package com.example.offerdaysongs.repository;

import com.example.offerdaysongs.model.License;
import com.example.offerdaysongs.model.Recording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long>, JpaSpecificationExecutor<License> {
    List<License> findByRecording(Recording recording);
}
