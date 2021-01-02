package com.seasunny.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/net")
public class NetController {

    @GetMapping("/info")
    public String net(HttpServletRequest httpServletRequest){
        return httpServletRequest.getLocalAddr() +":" +  httpServletRequest.getLocalPort();
    }



}
