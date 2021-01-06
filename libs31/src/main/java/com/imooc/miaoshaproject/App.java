package com.imooc.miaoshaproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.imooc.miaoshaproject.dao")
@EnableSwagger2
public class App {

    public static void main( String[] args ) {
        SpringApplication.run(App.class,args);
    }
}
