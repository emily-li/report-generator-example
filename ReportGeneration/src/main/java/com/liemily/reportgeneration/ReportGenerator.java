package com.liemily.reportgeneration;

import com.liemily.reportgeneration.domain.*;
import com.liemily.reportgeneration.exceptions.ReportGenerationException;
import com.liemily.stock.Stock;
import com.liemily.stock.StockService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Emily Li on 14/08/2017.
 */
@Component
@Lazy
public class ReportGenerator {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private StockService stockService;
    private ReportWriter reportWriter;

    @Autowired
    public ReportGenerator(StockService stockService, ReportWriter reportWriter) {
        this.stockService = stockService;
        this.reportWriter = reportWriter;
    }

    public Report generate(ReportRequest reportRequest) throws ReportGenerationException {
        ReportItems reportItems = generateReportItems(reportRequest);
        Path reportPath = generateReportPath(reportRequest.getFileType());
        reportWriter.write(reportItems, reportRequest.getFileType(), reportPath);
        return new Report(reportPath, reportRequest);
    }

    private ReportItems generateReportItems(ReportRequest reportRequest) {
        List<ReportItem> reportItems = new ArrayList<>();
        List<Stock> stocks = new ArrayList<>();
        if (reportRequest.getReportName().equals(REPORT_NAME.STOCK_REPORT)) {
            stocks = getStocks(reportRequest.getSortDirection());
        }

        stocks.forEach(stock -> reportItems.add(new ReportItem(stock.getSymbol(), stock.getSymbol(), stock.getValue(), stock.getVolume(), new BigDecimal(0))));
        return new ReportItems(reportItems);
    }

    private List<Stock> getStocks(Sort.Direction sortDirection) {
        Sort sort = new Sort(sortDirection, "value");
        return stockService.getStocks(sort);
    }

    private Path generateReportPath(FILE_TYPE fileType) {
        Path path = Paths.get(UUID.randomUUID().toString() + "." + fileType.toString().toLowerCase());
        path.toFile().deleteOnExit();
        return path;
    }
}
