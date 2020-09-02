package com.mooc.house.api.controller;

import com.mooc.house.api.dao.common.RestResponse;
import com.mooc.house.api.dao.house.model.House;
import com.mooc.house.api.service.HouseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomepageController {

    @Autowired
    private HouseService houseService;

    @GetMapping("/index")
    public String homePage(ModelMap modelMap){

        // get latest houses
        List<House> houses = houseService.getLatest();

        // set model map
        modelMap.put("recomHouses", houses);

        return "/homepage/index";
    }

    @GetMapping
    public String index(ModelMap modelMap){
        return "redirect:/index";
    }

    @GetMapping("/test/homepage/index")
    @ResponseBody
    public RestResponse<List<House>> getLatestHouses(){
        // get latest houses
        List<House> houses = houseService.getLatest();

        return RestResponse.success(houses);
    }

}
