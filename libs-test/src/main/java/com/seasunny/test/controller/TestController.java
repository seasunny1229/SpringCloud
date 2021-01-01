package com.seasunny.test.controller;

import com.seasunny.test.dto.UserDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello(){
        return "hello world";
    }

    @GetMapping("/string/{str}")
    public String string(@PathVariable("str") String string){
        return string;
    }



}
