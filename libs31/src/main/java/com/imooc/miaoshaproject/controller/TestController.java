package com.imooc.miaoshaproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String test(){
        return "hello miaosha";
    }

    @PostMapping("/user")
    public String user(
            @RequestParam("name") String name,
            @RequestParam("id") String id) {
        return name + "_" + id;
    }

    @GetMapping("/net")
    public String net(HttpServletRequest httpServletRequest){
        return httpServletRequest.getLocalAddr() +":" +  httpServletRequest.getLocalPort();
    }



}
