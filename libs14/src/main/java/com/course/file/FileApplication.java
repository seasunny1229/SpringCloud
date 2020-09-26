package com.course.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.course.file.mapper")
public class FileApplication {
    public static void main(String[] args){
        SpringApplication.run(FileApplication.class);
    }




}
