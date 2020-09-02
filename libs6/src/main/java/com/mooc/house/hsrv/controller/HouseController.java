package com.mooc.house.hsrv.controller;

import com.mooc.house.hsrv.common.LimitOffset;
import com.mooc.house.hsrv.common.RestResponse;
import com.mooc.house.hsrv.model.House;
import com.mooc.house.hsrv.model.HouseQueryRequest;
import com.mooc.house.hsrv.model.ListResponse;
import com.mooc.house.hsrv.service.HouseService;
import com.mooc.house.hsrv.service.RecommendService;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private RecommendService recommendService;

    @PostMapping("/list")
    public RestResponse<ListResponse<House>> getHouses(@RequestBody HouseQueryRequest houseQueryRequest){
        House query = houseQueryRequest.getHouse();
        Integer limit = houseQueryRequest.getLimit();
        Integer offset = houseQueryRequest.getOffset();

        LimitOffset limitOffset = LimitOffset.build(limit, offset);
        Pair<List<House>, Long> pair = houseService.queryHouse(query, limitOffset);

        ListResponse<House> listResponse = ListResponse.build(pair.getLeft(), pair.getRight());
        return RestResponse.success(listResponse);
    }

    @GetMapping("/detail/{id}")
    public RestResponse<House> getHouseDetail(@PathVariable("id") Long id){

        // query house
        House house = houseService.queryOneHouse(id);

        // increase hot
        recommendService.increaseHot(id);

        return RestResponse.success(house);
    }

    @PostMapping("/rating")
    public RestResponse<Object> updateHouseRating(@RequestBody HouseQueryRequest houseQueryRequest){
        Long id = houseQueryRequest.getHouse().getId();
        Double rating = houseQueryRequest.getHouse().getRating();
        int result = houseService.updateRating(id, rating);
        return RestResponse.success(result);
    }
}
