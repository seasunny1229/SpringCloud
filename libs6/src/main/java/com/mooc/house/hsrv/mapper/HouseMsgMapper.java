package com.mooc.house.hsrv.mapper;

import com.mooc.house.hsrv.model.UserMsg;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HouseMsgMapper {

    int insertUserMsg(@Param("message") UserMsg userMsg);

}
