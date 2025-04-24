package com.ridhoazh.obs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.ridhoazh.obs")
public class RidhoazhObsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RidhoazhObsApplication.class, args);
    }
}
