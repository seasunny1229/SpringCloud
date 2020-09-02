package com.mooc.house.hsrv.service;

import com.mooc.house.hsrv.mapper.HouseMsgMapper;
import com.mooc.house.hsrv.model.UserMsg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserMsgService {

    @Autowired
    private HouseMsgMapper houseMsgMapper;

    public int insertUserMsg(UserMsg userMsg){

        // null check
        if(userMsg.getCreateTime() == null){
            userMsg.setCreateTime(new Date());
        }
        if(userMsg.getUserName() == null){
            userMsg.setUserName("");
        }

        // update house message
        int result = houseMsgMapper.insertUserMsg(userMsg);

        // send email to agent

        return result;
    }


}
