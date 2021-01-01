package com.seasunny.test.controller;

import com.alibaba.fastjson.JSON;
import com.seasunny.test.dto.RedisDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequestMapping("/redis")
@RestController
public class RedisController {

    @Autowired
    private StringRedisTemplate redisTemplate;



    @GetMapping("/{key}")
    public String getRedisValue(@PathVariable("key") String key){
        String value = redisTemplate.opsForValue().get(key);
        if(value == null || value.equals("")){
            return "null";
        }
        return value;
    }

    @PostMapping("/put")
    public String putRedisValue(@ModelAttribute RedisDto redisDto){

        TimeUnit timeUnit = TimeUnit.SECONDS;
        if(redisDto.getUnit().equals(TimeUnit.MILLISECONDS.name())){
            timeUnit = TimeUnit.MILLISECONDS;
        }
        else if(redisDto.getUnit().equals(TimeUnit.MINUTES.name())){
            timeUnit = TimeUnit.MINUTES;
        }
        else if(redisDto.getUnit().equals(TimeUnit.HOURS.name())){
            timeUnit = TimeUnit.HOURS;
        }
        redisTemplate.opsForValue().set(
                redisDto.getKey(),
                redisDto.getValue(),
                redisDto.getDuration(),
                timeUnit
        );

        return "success";
    }

    @PostMapping("/keys")
    public Set<String> getKeys(@RequestParam("pattern") String pattern){
        return redisTemplate.keys(pattern);
    }


}
