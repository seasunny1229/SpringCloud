package com.mooc.house.api;

import com.seasunny.libs_test.MyClass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
//@EnableZuulProxy
@EnableCircuitBreaker
@EnableSwagger2
public class ApiGatewayApplication {

    public static void main(String[] args){
        MyClass.test();
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
