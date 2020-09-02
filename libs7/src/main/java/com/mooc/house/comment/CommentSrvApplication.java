package com.mooc.house.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CommentSrvApplication {
    public static void main(String[] args){
        SpringApplication.run(CommentSrvApplication.class, args);
    }
}
