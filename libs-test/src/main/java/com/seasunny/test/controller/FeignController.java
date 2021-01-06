package com.seasunny.test.controller;

import com.seasunny.test.feign.AuthenticationFeignClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign")
public class FeignController {

    @Autowired
    private AuthenticationFeignClient authenticationFeignClient;

    @GetMapping("/authentication/net")
    public String authenticationNet(){
        return authenticationFeignClient.testNet();
    }



}
