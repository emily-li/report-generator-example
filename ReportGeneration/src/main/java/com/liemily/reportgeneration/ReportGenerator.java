package com.liemily.reportgeneration;

import com.liemily.reportgeneration.domain.*;
import com.liemily.reportgeneration.exceptions.ReportGenerationException;
import com.liemily.reportgeneration.exceptions.ReportMarshallingException;
import com.liemily.reportgeneration.exceptions.ReportWritingException;
import com.liemily.stock.Stock;
import com.liemily.stock.StockService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.nio.file.Files;
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

    @Autowired
    public ReportGenerator(StockService stockService) {
        this.stockService = stockService;
    }

    public Report generate(ReportRequest reportRequest) throws ReportGenerationException {
        ReportItems reportItems = generateReportItems(reportRequest);
        String reportContents = generateReport(reportItems, reportRequest.getFileType());

        Path reportPath = generateReportPath(reportRequest.getFileType());
        writeReport(reportContents, reportPath);
        return new Report(reportPath, reportRequest);
    }

    private List<Stock> getStocks(Sort.Direction sortDirection) {
        Sort sort = new Sort(sortDirection, "value");
        return stockService.getStocks(sort);
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

    private Path generateReportPath(FILE_TYPE fileType) {
        Path path = Paths.get(UUID.randomUUID().toString() + "." + fileType.toString().toLowerCase());
        path.toFile().deleteOnExit();
        return path;
    }

    private void writeReport(String report, Path path) throws ReportWritingException {
        try {
            Files.write(path, report.getBytes());
        } catch (IOException e) {
            logger.info("Failed to write report to " + path.toAbsolutePath());
            throw new ReportWritingException("Failed to write report to " + path.toAbsolutePath(), e);
        }
        logger.info("Wrote report to " + path.toAbsolutePath());
    }

    private String generateReport(ReportItems reportItems, FILE_TYPE fileType) throws ReportMarshallingException {
        String contents;
        if (fileType.equals(FILE_TYPE.XML)) {
            contents = generateXML(reportItems);
        } else {
            throw new UnsupportedOperationException("Unsupported report file type " + fileType);
        }
        return contents;
    }

    private String generateXML(ReportItems reportItems) throws ReportMarshallingException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ReportItems.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            try (StringWriter stringWriter = new StringWriter()) {
                marshaller.marshal(reportItems, stringWriter);
                return stringWriter.toString();
            }
        } catch (JAXBException | IOException e) {
            String msg = "Failed to marshal report to string";
            logger.info(msg, e);
            throw new ReportMarshallingException(msg, e);
        }
    }
}
