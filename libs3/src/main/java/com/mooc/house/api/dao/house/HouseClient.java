package com.mooc.house.api.dao.house;

import com.mooc.house.api.dao.common.RestResponse;
import com.mooc.house.api.dao.house.model.City;
import com.mooc.house.api.dao.house.model.Community;
import com.mooc.house.api.dao.house.model.House;
import com.mooc.house.api.dao.house.model.HouseQueryRequest;
import com.mooc.house.api.dao.house.model.HouseUserRequest;
import com.mooc.house.api.dao.house.model.ListResponse;
import com.mooc.house.api.dao.house.model.UserMsg;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("house")
public interface HouseClient {

    @GetMapping("cities")
    RestResponse<List<City>> getAllCities();

    @GetMapping("communities")
    RestResponse<List<Community>> getAllCommunities();

    @PostMapping("house/list")
    RestResponse<ListResponse<House>> getHouses(@RequestBody HouseQueryRequest houseQueryRequest);

    @GetMapping("house/detail/{id}")
    RestResponse<House> getHouseDetail(@PathVariable("id") Long id);

    @PostMapping("house/rating")
    RestResponse<Object> updateHouseRating(@RequestBody HouseQueryRequest houseQueryRequest);

    @PostMapping("messages")
    RestResponse<Object> insertHouseMessage(@RequestBody UserMsg userMsg);

    @PostMapping("users/bind")
    RestResponse<Object> bind(@RequestBody HouseUserRequest houseUserRequest);

    @GetMapping("recommend/hot")
    RestResponse<List<House>> getHotHouses(@RequestParam("size") Integer size);

    @GetMapping("recommend/latest")
    RestResponse<List<House>> getLatestHouses(@RequestParam("size") Integer size);
}
