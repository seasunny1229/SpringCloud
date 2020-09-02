package com.mooc.house.hsrv.service;

import com.mooc.house.hsrv.mapper.CityMapper;
import com.mooc.house.hsrv.model.City;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityMapper cityMapper;

    public List<City> getAllCities(){
        City query = new City();
        return cityMapper.selectCities(query);
    }


}
