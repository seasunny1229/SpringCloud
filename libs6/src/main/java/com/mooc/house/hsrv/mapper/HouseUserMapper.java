package com.mooc.house.hsrv.mapper;

import com.mooc.house.hsrv.model.HouseUser;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseUserMapper {

    int insertHouseUser(@Param("house_user") HouseUser houseUser);

    int deleteHouseUser(@Param("house_id") Long houseId, @Param("user_id") Long userId, @Param("type") Integer type);

    List<HouseUser> selectHouseUsers(@Param("house_id") Long houseId, @Param("user_id") Long userId, @Param("type") Integer type);

    List<HouseUser> selectHouseUsersByHouseId(@Param("house_id") Long houseId, @Param("type") Integer type);
}
