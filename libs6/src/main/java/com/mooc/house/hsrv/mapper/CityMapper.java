package com.mooc.house.hsrv.mapper;

import com.mooc.house.hsrv.model.City;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityMapper {

    /**
     * City
     */
    // select all cities or select cities by condition
    public List<City> selectCities(City city);


}
