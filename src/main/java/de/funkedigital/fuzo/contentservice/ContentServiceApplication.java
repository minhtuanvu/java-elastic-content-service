package de.funkedigital.fuzo.contentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
@EnableScheduling
@EnableCaching
@ComponentScan("de.funkedigital.fuzo")
public class ContentServiceApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ContentServiceApplication.class, args);
    }
}
