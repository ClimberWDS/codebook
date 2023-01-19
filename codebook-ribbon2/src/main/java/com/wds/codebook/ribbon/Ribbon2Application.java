package com.wds.codebook.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Ribbon2Application {

    public static void main(String[] args) {
        SpringApplication.run(Ribbon2Application.class);
    }
}
