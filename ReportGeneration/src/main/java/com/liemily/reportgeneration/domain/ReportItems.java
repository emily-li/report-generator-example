package com.liemily.reportgeneration.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * Created by Emily Li on 18/08/2017.
 */
@XmlRootElement(name = "Report")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportItems {
    private Collection<ReportItem> reportItemCollection;

    public ReportItems() {
    }

    public ReportItems(Collection<ReportItem> reportItemCollection) {
        this.reportItemCollection = reportItemCollection;
    }

    public Collection<ReportItem> getReportItemCollection() {
        return reportItemCollection;
    }
}
