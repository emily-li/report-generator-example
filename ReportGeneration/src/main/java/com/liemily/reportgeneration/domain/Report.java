package com.liemily.reportgeneration.domain;

import java.nio.file.Path;

/**
 * Created by Emily Li on 19/08/2017.
 */
public class Report {
    private Path location;
    private ReportRequest reportRequest;

    public Report(Path location, ReportRequest reportRequest) {
        this.location = location;
        this.reportRequest = reportRequest;
    }

    public Path getLocation() {
        return location;
    }

    public ReportRequest getReportRequest() {
        return reportRequest;
    }
}
