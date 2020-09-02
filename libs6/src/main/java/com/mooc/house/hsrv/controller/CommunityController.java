package com.mooc.house.hsrv.controller;

import com.mooc.house.hsrv.common.RestResponse;
import com.mooc.house.hsrv.model.Community;
import com.mooc.house.hsrv.service.CommunityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("communities")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @GetMapping
    public RestResponse<List<Community>> getAllCommunities(){
        List<Community> communities = communityService.getAllCommunities();
        return RestResponse.success(communities);
    }


}
