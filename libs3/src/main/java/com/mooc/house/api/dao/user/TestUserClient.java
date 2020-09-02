package com.mooc.house.api.dao.user;

import com.mooc.house.api.dao.common.RestResponse;
import com.mooc.house.api.dao.user.model.Agency;
import com.mooc.house.api.dao.user.model.ListResponse;
import com.mooc.house.api.dao.user.model.User;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user", configuration = TestUserFeignConfig.class)
public interface TestUserClient {

    // http://USER/test
    @GetMapping(value = "test")
    String test();

    // http://USER/test/numbers/100
    @GetMapping(value = "test/numbers/{input}")
    String testNumbers(@PathVariable("input") int input);

    // http://USER/test/strings?input=test
    @GetMapping(value = "test/strings")
    String testStrings(@RequestParam("input") String input);

    @PostMapping("agencies")
    RestResponse<Object> addAgency(@RequestBody Agency agency);

    @GetMapping("agencies/all")
    RestResponse<List<Agency>> getAllAgencies();

    @GetMapping("agencies/{id}")
    RestResponse<Agency> getAgencyById(@PathVariable("id") Integer id);

    @GetMapping("agents/list")
    RestResponse<ListResponse<User>> getAgentsByLimit(@RequestParam("limit") Integer limit, @RequestParam("offset") Integer offset);

    @GetMapping("agents/{id}")
    RestResponse<User> getAgentDetail(@PathVariable("id") Long id);

    @PostMapping("users/register")
    RestResponse<Object> register(@RequestBody User user);

    @PostMapping("users/enable")
    RestResponse<Object> enable(@RequestBody String key);

    @GetMapping("users/{id}")
    RestResponse<User> getUserById(Long id);

    @PostMapping("users/list")
    RestResponse<List<User>> getUsers(@RequestBody User user);

    @PostMapping("users/update")
    RestResponse<User> update(@RequestBody User user);

    @PostMapping("users/login")
    RestResponse<User> login(@RequestBody User user);

    @PostMapping("users/logout")
    RestResponse<Object> logout(@RequestBody String token);



}
