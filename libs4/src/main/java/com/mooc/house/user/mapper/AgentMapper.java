package com.mooc.house.user.mapper;

import com.mooc.house.user.common.PageParams;
import com.mooc.house.user.model.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgentMapper {

    List<User> selectAgent(@Param("user") User user, @Param("pageParams") PageParams pageParams);

    Long selectAgentCount(@Param("user") User user);

}
