package com.example.offerdaysongs.controller;

import com.example.offerdaysongs.model.License;
import com.example.offerdaysongs.service.LicenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/clients/recordings")
public class ClientRecordingController {
    private static final String ID = "id";

    private final LicenseService licenseService;

    public ClientRecordingController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/{id:[\\d]+}/cost")
    public ResponseEntity<Object> getRecordingCost(@PathVariable(ID) Long id) {
        var licenses = licenseService.getByRecording(id);

        return (licenses.isEmpty()) ?
                ResponseEntity.badRequest().build() :
                new ResponseEntity<>(licenses.stream()
                        .map(License::getCost)
                        .reduce(BigDecimal.ZERO, BigDecimal::add), HttpStatus.OK);
    }
}
