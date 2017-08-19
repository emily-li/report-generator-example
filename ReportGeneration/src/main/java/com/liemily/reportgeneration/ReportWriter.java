package com.liemily.reportgeneration;

import com.liemily.reportgeneration.domain.FILE_TYPE;
import com.liemily.reportgeneration.domain.ReportItems;
import com.liemily.reportgeneration.exceptions.ReportGenerationException;
import com.liemily.reportgeneration.exceptions.ReportMarshallingException;
import com.liemily.reportgeneration.exceptions.ReportWritingException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Emily Li on 19/08/2017.
 */
@Component
@Lazy
public class ReportWriter {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    void write(ReportItems reportItems, FILE_TYPE fileType, Path path) throws ReportGenerationException {
        String reportItemsString = generateReport(reportItems, fileType);
        writeFile(reportItemsString, path);
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

    private void writeFile(String reportContents, Path path) throws ReportWritingException {
        try {
            Files.write(path, reportContents.getBytes());
        } catch (IOException e) {
            logger.info("Failed to write report to " + path.toAbsolutePath());
            throw new ReportWritingException("Failed to write report to " + path.toAbsolutePath(), e);
        }
        logger.info("Wrote report to " + path.toAbsolutePath());
    }
}
