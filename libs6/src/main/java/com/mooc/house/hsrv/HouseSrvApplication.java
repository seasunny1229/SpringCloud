package com.mooc.house.hsrv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class HouseSrvApplication {
    public static void main(String[] args){
        SpringApplication.run(HouseSrvApplication.class, args);
    }

}
