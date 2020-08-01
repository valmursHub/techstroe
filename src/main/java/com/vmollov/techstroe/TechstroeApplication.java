package com.vmollov.techstroe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching
@EnableAsync
@SpringBootApplication
public class TechstroeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechstroeApplication.class, args);
    }

}
