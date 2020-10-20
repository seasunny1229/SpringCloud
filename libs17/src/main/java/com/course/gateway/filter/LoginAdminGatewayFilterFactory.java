package com.course.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LoginAdminGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Resource
    LoginAdminGatewayFilter loginAdminGatewayFilter;

    @Autowired
    private TestGatewayFilter testGatewayFilter;

    @Override
    public GatewayFilter apply(Object config) {
        return loginAdminGatewayFilter;
    }
}