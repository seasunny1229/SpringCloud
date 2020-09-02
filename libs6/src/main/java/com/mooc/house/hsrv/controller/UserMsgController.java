package com.mooc.house.hsrv.controller;

import com.mooc.house.hsrv.common.RestResponse;
import com.mooc.house.hsrv.model.UserMsg;
import com.mooc.house.hsrv.service.UserMsgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class UserMsgController {

    @Autowired
    private UserMsgService userMsgService;

    @PostMapping
    public RestResponse<Object> insertHouseMessage(@RequestBody UserMsg userMsg){
        int result = userMsgService.insertUserMsg(userMsg);
        return RestResponse.success(result);
    }

}
