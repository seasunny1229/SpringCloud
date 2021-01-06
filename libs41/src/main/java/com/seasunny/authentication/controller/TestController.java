package com.seasunny.authentication.controller;

import com.seasunny.authentication.domain.Member;
import com.seasunny.authentication.dto.MemberDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/hello")
    public String hello(){
        return "hello authentication";
    }

    @PostMapping("/dto/string")
    public String userDtoString(@RequestBody MemberDto memberDto){
        return memberDto.toString();
    }

    @PostMapping("/dto/json")
    public MemberDto memberDto(@RequestBody MemberDto memberDto){
        memberDto.setRegisterTime(new Date());
        return memberDto;
    }

    @GetMapping("/redis/{key}")
    public String getRedisValue(@PathVariable("key") String key){
        String value = redisTemplate.opsForValue().get(key);
        if(value == null || value.equals("")){
            return "null";
        }
        return value;
    }

    @GetMapping("/net")
    public String net(HttpServletRequest httpServletRequest){
        return httpServletRequest.getLocalAddr() + ":" +  httpServletRequest.getLocalPort();
    }

}
