package com.mooc.house.api.config;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HystrixConfig {

    @Bean
    public ServletRegistrationBean hystrixMetricsStreamServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean<>(new HystrixMetricsStreamServlet());
        registration.setLoadOnStartup(1);
        registration.addUrlMappings("/hystrix.stream");
        registration.setName("HystrixMetricsStreamServlet");
        return registration;
    }


}
