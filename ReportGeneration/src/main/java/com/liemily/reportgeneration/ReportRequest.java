package com.liemily.reportgeneration;

import com.liemily.reportgeneration.reports.FILE_TYPE;
import com.liemily.reportgeneration.reports.REPORT_NAME;
import org.springframework.data.domain.Sort;

/**
 * Created by Emily Li on 18/08/2017.
 */
public class ReportRequest {
    private final REPORT_NAME reportName;
    private final FILE_TYPE fileType;
    private final Sort.Direction sortDirection;

    public ReportRequest(REPORT_NAME reportName, FILE_TYPE fileType) {
        this.reportName = reportName;
        this.fileType = fileType;
        this.sortDirection = Sort.DEFAULT_DIRECTION;
    }

    public ReportRequest(REPORT_NAME reportName, FILE_TYPE fileType, Sort.Direction sortDirection) {
        this.reportName = reportName;
        this.fileType = fileType;
        this.sortDirection = sortDirection;
    }

    public REPORT_NAME getReportName() {
        return reportName;
    }

    public FILE_TYPE getFileType() {
        return fileType;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }
}
