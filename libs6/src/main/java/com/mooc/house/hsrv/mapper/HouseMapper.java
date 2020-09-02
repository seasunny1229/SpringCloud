package com.mooc.house.hsrv.mapper;

import com.mooc.house.hsrv.common.LimitOffset;
import com.mooc.house.hsrv.model.House;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * house
 *
 */

@Mapper
public interface HouseMapper {

    // select all houses or select houses by condition
    List<House> selectHouses(House house);

    // select houses by condition and page limit
    List<House> selectHouseByLimit(
            @Param("house")House house,
            @Param("pageParams")LimitOffset limitOffset,
            @Param("order") String order
    );

    // get house count by condition
    Long getNumberHouses(@Param("house")House house);

    // update house info like house rating
    int updateHouse(@Param("house") House house);

}
