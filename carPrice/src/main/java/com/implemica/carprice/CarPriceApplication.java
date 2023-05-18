package com.implemica.carprice;

import com.implemica.carprice.model.Price;
import com.implemica.carprice.repository.PriceRepository;
import com.implemica.carprice.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;

@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class})
public class CarPriceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarPriceApplication.class, args);
    }

}
