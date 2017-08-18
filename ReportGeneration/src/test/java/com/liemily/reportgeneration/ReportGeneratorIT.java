package com.liemily.reportgeneration;

import com.liemily.reportgeneration.domain.*;
import com.liemily.stock.Stock;
import com.liemily.stock.StockRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Emily Li on 14/08/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportGeneratorIT {
    @Autowired
    private ReportGenerator reportGenerator;

    @Autowired
    private StockRepository stockRepository;

    private Collection<Stock> repositoryStocks;

    @Before
    public void setup() {
        repositoryStocks = new HashSet<>();
        IntStream.range(0, 3).forEach(i -> repositoryStocks.add(new Stock("SYM" + i, new BigDecimal(i * i), 1)));
        stockRepository.save(repositoryStocks);
        repositoryStocks = stockRepository.findAll(); // Required to make the BigDecimal value consistent with the checks that retrieve stocks from the database later
    }

    @Test
    public void testGetStockValues() throws Exception {
        final Sort.Direction[] DIRECTIONS = new Sort.Direction[]{Sort.Direction.ASC, Sort.Direction.DESC};
        final Map<Sort.Direction, BigDecimal[]> EXPECTED_DIRECTION_VALUES = new HashMap<>();

        TreeSet<BigDecimal> stockValues = new TreeSet<>();
        repositoryStocks.forEach(stock -> stockValues.add(stock.getValue()));

        EXPECTED_DIRECTION_VALUES.put(Sort.Direction.ASC, stockValues.toArray(new BigDecimal[repositoryStocks.size()]));
        EXPECTED_DIRECTION_VALUES.put(Sort.Direction.DESC, stockValues.descendingSet().toArray(new BigDecimal[repositoryStocks.size()]));

        for (Sort.Direction direction : DIRECTIONS) {
            ReportRequest reportRequest = new ReportRequest(REPORT_NAME.STOCK_REPORT, FILE_TYPE.XML, direction);
            Path reportPath = reportGenerator.generate(reportRequest).getLocation();

            List<Stock> marshalledStocks = getStocksFromXML(reportPath);
            List<BigDecimal> marshalledStockValues = new ArrayList<>();
            marshalledStocks.forEach(stock -> marshalledStockValues.add(stock.getValue()));

            assertArrayEquals(EXPECTED_DIRECTION_VALUES.get(direction), marshalledStockValues.toArray(new BigDecimal[repositoryStocks.size()]));
        }
    }

    private List<Stock> getStocksFromXML(Path xmlPath) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(ReportItems.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ReportItems reportItems = (ReportItems) unmarshaller.unmarshal(xmlPath.toFile());

        List<Stock> marshalledStocks = new ArrayList<>();
        reportItems.getReportItemCollection().forEach(reportItem -> marshalledStocks.add(new Stock(reportItem.getStockSymbol(), reportItem.getValue(), reportItem.getVolume())));
        return marshalledStocks;
    }
}
