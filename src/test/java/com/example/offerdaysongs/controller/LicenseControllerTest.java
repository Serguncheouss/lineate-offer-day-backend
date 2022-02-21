package com.example.offerdaysongs.controller;

import com.example.offerdaysongs.mock.MockDataProvider;
import com.example.offerdaysongs.model.Company;
import com.example.offerdaysongs.model.License;
import com.example.offerdaysongs.model.Recording;
import com.example.offerdaysongs.model.Singer;
import com.example.offerdaysongs.service.LicenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LicenseController.class)
class LicenseControllerTest {
    private static final String LICENSE_ENDPOINT = "/api/licenses";
    private static final List<License> licenses = MockDataProvider.getLicences();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    LicenseService licenseService;

    private MockHttpServletRequestBuilder requestBuilder;

    @BeforeEach
    void setUp() {
        requestBuilder = get(LICENSE_ENDPOINT).contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void getAllLicenses_returnSuccessAndValidJsonStructure() throws Exception {
        when(licenseService.getAll(null, null, null)).thenReturn(licenses);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[3].id", is(4)))
                .andExpect(jsonPath("$[3].recording.id", is(2)))
                .andExpect(jsonPath("$[3].recording.title", is("Title2")))
                .andExpect(jsonPath("$[3].recording.version", is("1")))
                .andExpect(jsonPath("$[3].recording.releaseTime", is("1990-03-25T00:00:00+03:00")))
                .andExpect(jsonPath("$[3].recording.singer.id", is(2)))
                .andExpect(jsonPath("$[3].recording.singer.name", is("Singer2")))
                .andExpect(jsonPath("$[3].startTime", is("2015-08-05T00:00:00+03:00")))
                .andExpect(jsonPath("$[3].endTime", is("2020-04-05T00:00:00+03:00")))
                .andExpect(jsonPath("$[3].cost", is(5.3)))
                .andExpect(jsonPath("$[3].company.id", is(3)))
                .andExpect(jsonPath("$[3].company.name", is("Company3")));
    }

    @Test
    public void givenCompanyId_getLicenses_returnListWith2LicencesIds1And3() throws Exception {
        var companyId = 1L;

        when(licenseService.getAll(null, null, companyId))
                .thenReturn(licenses.stream()
                        .filter(l -> l.getCompany().getId() == companyId)
                        .collect(Collectors.toList()));

        requestBuilder = requestBuilder.param("company_id", String.valueOf(companyId));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(3)));
    }

    @Test
    public void givenStartTime_getLicenses_returnListWith3LicencesIds1And2And4() throws Exception {
        var startTime = ZonedDateTime.parse("2018-01-13T00:00:00+03:00");
        when(licenseService.getAll(startTime, null, null))
                .thenReturn(licenses.stream()
                        .filter(l -> l.getStartTime().isAfter(startTime) || l.getStartTime().isEqual(startTime) ||
                                l.getEndTime().isAfter(startTime) || l.getEndTime().isEqual(startTime))
                        .collect(Collectors.toList()));

        requestBuilder = requestBuilder.param("start_time", startTime.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(4)));
    }

    @Test
    public void givenEndTime_getLicenses_returnListWith3LicencesIds2And3And4() throws Exception {
        var endTime = ZonedDateTime.parse("2020-04-05T00:00:00+03:00");
        when(licenseService.getAll(null, endTime, null))
                .thenReturn(licenses.stream()
                        .filter(l -> l.getEndTime().isBefore(endTime) || l.getEndTime().isEqual(endTime) ||
                                l.getStartTime().isBefore(endTime) || l.getStartTime().isEqual(endTime))
                        .collect(Collectors.toList()));

        requestBuilder = requestBuilder.param("end_time", endTime.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[1].id", is(3)))
                .andExpect(jsonPath("$[2].id", is(4)));
    }

    @Test
    public void givenInterval_getLicenses_returnListWith2LicencesIds2And4() throws Exception {
        var startTime = ZonedDateTime.parse("2018-01-13T00:00:00+03:00");
        var endTime = ZonedDateTime.parse("2020-03-13T00:00:00+03:00");
        when(licenseService.getAll(startTime, endTime, null))
                .thenReturn(licenses.stream()
                        .filter(l -> (
                                l.getStartTime().isAfter(startTime) || l.getStartTime().isEqual(startTime) ||
                                        l.getEndTime().isAfter(startTime) || l.getEndTime().isEqual(startTime)) && (
                                                l.getEndTime().isBefore(endTime) || l.getEndTime().isEqual(endTime) ||
                                                        l.getStartTime().isBefore(endTime) || l.getStartTime().isEqual(endTime)))
                        .collect(Collectors.toList()));

        requestBuilder = requestBuilder
                .param("start_time", startTime.toString())
                .param("end_time", endTime.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[1].id", is(4)));
    }

    @Test
    public void givenCompanyIdAndInterval_getLicenses_returnListWith1LicenceIds4() throws Exception {
        var companyId = 3L;
        var startTime = ZonedDateTime.parse("2018-01-13T00:00:00+03:00");
        var endTime = ZonedDateTime.parse("2020-04-05T00:00:00+03:00");
        when(licenseService.getAll(startTime, endTime, companyId))
                .thenReturn(licenses.stream()
                        .filter(l -> (
                                l.getStartTime().isAfter(startTime) || l.getStartTime().isEqual(startTime) ||
                                        l.getEndTime().isAfter(startTime) || l.getEndTime().isEqual(startTime)) && (
                                                l.getEndTime().isBefore(endTime) || l.getEndTime().isEqual(endTime) ||
                                                        l.getStartTime().isBefore(endTime) || l.getStartTime().isEqual(endTime)) &&
                                l.getCompany().getId() == companyId)
                        .collect(Collectors.toList()));

        requestBuilder = requestBuilder
                .param("start_time", startTime.toString())
                .param("end_time", endTime.toString())
                .param("company_id", String.valueOf(companyId));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(4)));
    }



    private Singer createSinger(Long id, String name) {
        var singer = new Singer();
        singer.setId(id);
        singer.setName(name);

        return singer;
    }

    private Recording createRecording(Long id, String title, String version, ZonedDateTime releaseTime, Singer singer) {
        var recording = new Recording();
        recording.setId(id);
        recording.setTitle(title);
        recording.setVersion(version);
        recording.setReleaseTime(releaseTime);
        recording.setSinger(singer);

        return recording;
    }

    private Company createCompany(Long id, String name) {
        var company = new Company();
        company.setId(id);
        company.setName(name);

        return company;
    }

    private License createLicense(Long id, Recording recording, ZonedDateTime startTime, ZonedDateTime endTime,
                                  BigDecimal cost, Company company) {
        var license = new License();
        license.setId(id);
        license.setRecording(recording);
        license.setStartTime(startTime);
        license.setEndTime(endTime);
        license.setCost(cost);
        license.setCompany(company);

        return license;
    }
}