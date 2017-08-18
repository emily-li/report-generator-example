package com.liemily.reportgeneration;

import com.liemily.stock.config.StockConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Created by Emily Li on 14/08/2017.
 */
@SpringBootApplication
@Import(StockConfig.class)
public class ReportGenerationApp {
    public static void main(String[] args) {
        SpringApplication.run(ReportGenerationApp.class, args);
    }
}
