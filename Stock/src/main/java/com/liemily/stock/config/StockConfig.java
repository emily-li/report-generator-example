package com.liemily.stock.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

/**
 * Created by Emily Li on 18/08/2017.
 */
@Configuration
@ComponentScan("com.liemily.stock")
@EnableJpaRepositories("com.liemily.stock")
@EntityScan("com.liemily.stock")
@Lazy
public class StockConfig {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @PostConstruct
    public void postConstruct() {
        logger.info("Instantiated " + getClass().getSimpleName());
    }
}
