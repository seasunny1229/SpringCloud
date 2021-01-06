package com.seasunny.authentication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *
 * SSO认证中心
 *
 * 用户注册
 * 用户登录
 * 用户登出
 *
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.seasunny.authentication.mapper")
public class AuthenticationApplication {

    public static void main(String... args){
        SpringApplication.run(AuthenticationApplication.class);
    }



}
