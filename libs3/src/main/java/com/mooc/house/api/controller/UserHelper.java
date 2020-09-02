package com.mooc.house.api.controller;

import com.google.common.collect.Range;
import com.mooc.house.api.common.ResultMsg;
import com.mooc.house.api.dao.user.model.User;

import org.apache.commons.lang3.StringUtils;

public class UserHelper {

    public static ResultMsg validate(User account){


        if(StringUtils.isBlank(account.getEmail())){
            return ResultMsg.errorMsg("wrong email");
        }

        if(StringUtils.isBlank(account.getName())){
            return ResultMsg.errorMsg("wrong name");
        }

        if (StringUtils.isBlank(account.getConfirmPasswd()) || StringUtils.isBlank(account.getPasswd()) || !account.getPasswd().equals(account.getConfirmPasswd())) {
            return ResultMsg.errorMsg("wrong password");
        }

        if(account.getPasswd().length() < 6){
            return ResultMsg.errorMsg("password size is wrong");
        }

        if(account.getType() == null || !Range.closed(1,2).contains(account.getType())){
            return ResultMsg.errorMsg("wrong password");
        }

        return ResultMsg.success();
    }







}
