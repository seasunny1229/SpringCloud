package com.mooc.house.hsrv.controller;

import com.mooc.house.hsrv.common.RestResponse;
import com.mooc.house.hsrv.model.House;
import com.mooc.house.hsrv.service.RecommendService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @GetMapping("/hot")
    public RestResponse<List<House>> getHotHouses(@RequestParam("size") Integer size){
       List<House> houses = recommendService.getHotHouses(size);
       return RestResponse.success(houses);
    }

    @GetMapping("/latest")
    public RestResponse<List<House>> getLatestHouses(@RequestParam("size") Integer size){
        List<House> houses = recommendService.getLatest(size);
        return RestResponse.success(houses);
    }

}
