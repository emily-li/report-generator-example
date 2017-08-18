package com.liemily.reportgeneration.reports.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * Created by Emily Li on 18/08/2017.
 */
@XmlRootElement(name = "Stock")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportItem {
    private String stockSymbol;
    private String stockCompany;
    private BigDecimal value;
    private int volume;
    private BigDecimal gains;

    public ReportItem() {
    }

    public ReportItem(String stockSymbol, String stockCompany, BigDecimal value, int volume, BigDecimal gains) {
        this.stockSymbol = stockSymbol;
        this.stockCompany = stockCompany;
        this.value = value;
        this.volume = volume;
        this.gains = gains;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getStockCompany() {
        return stockCompany;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getVolume() {
        return volume;
    }

    public BigDecimal getGains() {
        return gains;
    }
}
