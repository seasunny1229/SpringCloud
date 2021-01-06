package com.seasunny.test.controller;

import com.seasunny.test.service.CacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guava")
public class GuavaCacheController {

    @Autowired
    private CacheService cacheService;

    @PostMapping("/set")
    public String set(@RequestParam("key") String key, @RequestParam("value") String value){
        cacheService.setCommonCache(key, value);
        return "success";
    }

    @GetMapping("/get/{key}")
    public String get(@PathVariable("key") String key){
        return (String) cacheService.getFromCommonCache(key);
    }


}
