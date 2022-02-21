package com.example.offerdaysongs.controller;

import com.example.offerdaysongs.mock.MockDataProvider;
import com.example.offerdaysongs.model.License;
import com.example.offerdaysongs.service.LicenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ClientRecordingController.class)
class ClientRecordingControllerTest {
    private static final String CLIENT_RECORDING_ENDPOINT = "/api/clients/recordings";
    private static final List<License> licenses = MockDataProvider.getLicences();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    LicenseService licenseService;

    @Test
    void givenRecordingId_getRecordingCost_return8_3() throws Exception {
        var recordingId = 2L;
        given(licenseService.getByRecording(recordingId)).willReturn(licenses.stream()
                .filter(l -> l.getRecording().getId() == recordingId)
                .collect(Collectors.toList()));

        mockMvc.perform(get("/api/clients/recordings/" + recordingId + "/cost").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(8.3)));
    }
}