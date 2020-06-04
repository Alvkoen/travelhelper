package com.alvcohen.travelhelper;

import com.alvcohen.travelhelper.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class TravelHelperApplication {
    public static void main(String[] args) {
        SpringApplication.run(TravelHelperApplication.class, args);
    }
}
