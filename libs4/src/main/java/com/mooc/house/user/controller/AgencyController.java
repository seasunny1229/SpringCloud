package com.mooc.house.user.controller;

import com.mooc.house.user.common.RestResponse;
import com.mooc.house.user.model.Agency;
import com.mooc.house.user.service.AgencyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agencies")
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    @PostMapping
    public RestResponse<Object> addAgency(@RequestBody Agency agency){
        int result = agencyService.addAgency(agency);
        return RestResponse.success(result);
    }

    @GetMapping("/all")
    public RestResponse<List<Agency>> getAllAgencies(){
        List<Agency> agencies = agencyService.getAllAgencies();
        return RestResponse.success(agencies);
    }

    @GetMapping("/{id}")
    public RestResponse<Agency> getAgencyById(@PathVariable("id") Integer id){
        Agency agency = agencyService.getAgencyById(id);
        return RestResponse.success(agency);
    }

    

}
