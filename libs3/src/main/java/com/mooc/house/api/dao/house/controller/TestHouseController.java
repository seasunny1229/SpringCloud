package com.mooc.house.api.dao.house.controller;


import com.mooc.house.api.dao.common.RestResponse;
import com.mooc.house.api.dao.house.HouseClient;
import com.mooc.house.api.dao.house.model.City;
import com.mooc.house.api.dao.house.model.Community;
import com.mooc.house.api.dao.house.model.House;
import com.mooc.house.api.dao.house.model.HouseQueryRequest;
import com.mooc.house.api.dao.house.model.HouseUserRequest;
import com.mooc.house.api.dao.house.model.ListResponse;
import com.mooc.house.api.dao.house.model.UserMsg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/house")
public class TestHouseController {

    @Autowired
    private HouseClient houseClient;

    @GetMapping("/cities")
    public RestResponse<List<City>> getCities(){
        return houseClient.getAllCities();
    }

    @GetMapping("/communities")
    public RestResponse<List<Community>> getCommunities(){
        return houseClient.getAllCommunities();
    }

    @GetMapping("/house/list")
    public RestResponse<ListResponse<House>> getHouseList(){
        HouseQueryRequest houseQueryRequest = new HouseQueryRequest();
        House house = new House();
        houseQueryRequest.setHouse(house);
        houseQueryRequest.setLimit(4);
        houseQueryRequest.setOffset(0);
        return houseClient.getHouses(houseQueryRequest);
    }

    @GetMapping("/house/detail")
    public RestResponse<House> getHouseDetail(){
        return houseClient.getHouseDetail(22L);
    }

    @GetMapping("/house/rating")
    public RestResponse<Object> updateHouseRating(){
        HouseQueryRequest houseQueryRequest = new HouseQueryRequest();
        House house = new House();
        house.setId(26L);
        house.setRating(3.8D);
        houseQueryRequest.setHouse(house);
        return houseClient.updateHouseRating(houseQueryRequest);
    }

    @GetMapping("/messages")
    public RestResponse<Object> addMessage(){
        UserMsg userMsg = new UserMsg();
        userMsg.setAgentId(7L);
        userMsg.setCreateTime(new Date());
        userMsg.setUserName("122334");
        userMsg.setHouseId(24L);
        userMsg.setMsg("hello world!!!");
        return houseClient.insertHouseMessage(userMsg);
    }

    @GetMapping("/users/bind")
    public RestResponse<Object> bindHouse(){
        HouseUserRequest houseUserRequest = new HouseUserRequest();
        houseUserRequest.setHouseId(28L);
        houseUserRequest.setUserId(38L);
        houseUserRequest.setBindType(2);
        return houseClient.bind(houseUserRequest);
    }

    @GetMapping("/users/unbind")
    public RestResponse<Object> unbindHouse(){
        HouseUserRequest houseUserRequest = new HouseUserRequest();
        houseUserRequest.setHouseId(28L);
        houseUserRequest.setUserId(38L);
        houseUserRequest.setBindType(2);
        houseUserRequest.setUnBind(true);
        return houseClient.bind(houseUserRequest);
    }

    @GetMapping("/recommend/hot")
    public RestResponse<List<House>> getHotHouses(){
        return houseClient.getHotHouses(3);
    }

    @GetMapping("/recommend/latest")
    public RestResponse<List<House>> getLatestHouses(){
        return houseClient.getHotHouses(3);
    }


}
