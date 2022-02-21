package com.example.offerdaysongs.mock;

import com.example.offerdaysongs.model.Company;
import com.example.offerdaysongs.model.License;
import com.example.offerdaysongs.model.Recording;
import com.example.offerdaysongs.model.Singer;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockDataProvider {
    public static List<License> getLicences() {
        var licenses = new ArrayList<License>();
        Company company1 = createCompany(1L, "Company1");
        licenses.add(createLicense(
                1L,
                createRecording(
                        1L,
                        "Title1",
                        "1",
                        ZonedDateTime.parse("1998-02-23T00:00:00+03:00"),
                        createSinger(1L, "Singer1")
                ),
                ZonedDateTime.parse("2020-10-15T00:00:00+03:00"),
                ZonedDateTime.parse("2025-10-15T00:00:00+03:00"),
                new BigDecimal("12.5"),
                company1
        ));
        Recording recording2 = createRecording(
                2L,
                "Title2",
                "1",
                ZonedDateTime.parse("1990-03-25T00:00:00+03:00"),
                createSinger(2L, "Singer2")
        );
        licenses.add(createLicense(
                2L,
                recording2,
                ZonedDateTime.parse("2018-01-13T00:00:00+03:00"),
                ZonedDateTime.parse("2020-03-13T00:00:00+03:00"),
                new BigDecimal("3"),
                createCompany(2L, "Company2")
        ));
        licenses.add(createLicense(
                3L,
                createRecording(
                        3L,
                        "Title3",
                        "1",
                        ZonedDateTime.parse("2005-01-05T00:00:00+03:00"),
                        createSinger(3L, "Singer3")
                ),
                ZonedDateTime.parse("2005-01-05T00:00:00+03:00"),
                ZonedDateTime.parse("2015-09-05T00:00:00+03:00"),
                new BigDecimal("1"),
                company1
        ));
        licenses.add(createLicense(
                4L,
                recording2,
                ZonedDateTime.parse("2015-08-05T00:00:00+03:00"),
                ZonedDateTime.parse("2020-04-05T00:00:00+03:00"),
                new BigDecimal("5.3"),
                createCompany(3L, "Company3")
        ));

        return licenses;
    }

    private static Singer createSinger(Long id, String name) {
        var singer = new Singer();
        singer.setId(id);
        singer.setName(name);

        return singer;
    }

    private static Recording createRecording(Long id, String title, String version, ZonedDateTime releaseTime, Singer singer) {
        var recording = new Recording();
        recording.setId(id);
        recording.setTitle(title);
        recording.setVersion(version);
        recording.setReleaseTime(releaseTime);
        recording.setSinger(singer);

        return recording;
    }

    private static Company createCompany(Long id, String name) {
        var company = new Company();
        company.setId(id);
        company.setName(name);

        return company;
    }

    private static License createLicense(Long id, Recording recording, ZonedDateTime startTime, ZonedDateTime endTime,
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
