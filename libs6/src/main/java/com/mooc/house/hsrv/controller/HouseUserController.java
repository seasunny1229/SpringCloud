package com.mooc.house.hsrv.controller;

import com.mooc.house.hsrv.common.HouseUserType;
import com.mooc.house.hsrv.common.RestResponse;
import com.mooc.house.hsrv.model.HouseUserRequest;
import com.mooc.house.hsrv.service.HouseUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class HouseUserController {

    @Autowired
    private HouseUserService houseUserService;

    @PostMapping("/bind")
    public RestResponse<Object> bind(@RequestBody HouseUserRequest houseUserRequest){

        // get bind type
        Integer bindType = houseUserRequest.getBindType();

        // house user type
        HouseUserType houseUserType  = bindType.equals(HouseUserType.SALE.value) ? HouseUserType.SALE : HouseUserType.BOOKMARK;

        // unbind
        int result;
        if(houseUserRequest.isUnBind()){
            result = houseUserService.unbindUserToHouse(houseUserRequest.getHouseId(), houseUserRequest.getUserId(), houseUserType);
        }

        // bind
        else {
            result = houseUserService.bindUserToHouse(houseUserRequest.getHouseId(), houseUserRequest.getUserId(), houseUserType);
        }

        return RestResponse.success(result);
    }


}
