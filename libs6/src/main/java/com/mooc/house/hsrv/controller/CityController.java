package com.mooc.house.hsrv.controller;

import com.mooc.house.hsrv.common.RestResponse;
import com.mooc.house.hsrv.model.City;
import com.mooc.house.hsrv.service.CityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public RestResponse<List<City>> getAllCities(){
        List<City> cities = cityService.getAllCities();
        return RestResponse.success(cities);
    }

}
