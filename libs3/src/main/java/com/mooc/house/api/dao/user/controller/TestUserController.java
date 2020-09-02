package com.mooc.house.api.dao.user.controller;

import com.mooc.house.api.config.GenericRest;
import com.mooc.house.api.dao.TestHystrixDao;
import com.mooc.house.api.dao.user.TestUserClient;
import com.mooc.house.api.dao.common.RestResponse;
import com.mooc.house.api.dao.user.model.Agency;
import com.mooc.house.api.dao.user.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/user")
public class TestUserController {

    @Autowired
    private GenericRest genericRest;

    @Autowired
    private TestUserClient testUserClient;

    @Autowired
    private TestHystrixDao testHystrixDao;

    @GetMapping
    public String test(){
        return "hello test user";
    }

    @GetMapping("/test")
    public String testUser(){
        String result = testUserClient.test();
        return "the result is: " + result;
    }

    @GetMapping("/numbers/{input}")
    public String testNumbers(@PathVariable("input") int input){
        String result = testUserClient.testNumbers(input);
        return "the result is: " + result;
    }

    @GetMapping("/strings")
    public String testStrings(@RequestParam("input") String input){
        String result = testUserClient.testStrings(input);
        return "the result is: " + result;
    }

    @GetMapping("/rest/numbers/{input}")
    public String testNumbersByRestTemplate(@PathVariable("input") int input){
        String url =  "http://user/test/numbers/" + input;
        ResponseEntity<String> result = genericRest.get(url, new ParameterizedTypeReference<String>() {});
        return "the result from rest template is: " + result.getBody();
    }


    @GetMapping("/rest/strings")
    public String testStringsByRestTemplate(@RequestParam("input") String input){
        String url =  "http://user/test/strings?input=" + input;
        ResponseEntity<String> result = genericRest.get(url, new ParameterizedTypeReference<String>() {});
        return "the result from rest template is: " + result.getBody();
    }


    @GetMapping("/hystrix/default")
    public String simulateDefaultTimeConsuming(){
        return testHystrixDao.simulateDefaultTimeConsuming();
    }

    @GetMapping("/hystrix/custom")
    public String simulateCustomTimeConsuming(@RequestParam("input") String input){
        return testHystrixDao.simulateCustomTimeConsuming(input);
    }

    @GetMapping("/agencies")
    public RestResponse<Object> addAgency(){
        Agency agency = new Agency();
        agency.setName("hello1");
        agency.setAboutUs("hello1");
        agency.setAddress("hello1");
        agency.setEmail("11551");
        agency.setPhone("11221");
        agency.setWebSite("gagg1");
        agency.setMobile("ga1");
        return testUserClient.addAgency(agency);
    }

    @GetMapping("/register")
    public RestResponse<Object> addUser(){
        User user = new User();
        user.setEmail("test@163.com");
        user.setPasswd("123456");
        user.setType(User.TYPE_USER);
        return testUserClient.register(user);
    }

}
