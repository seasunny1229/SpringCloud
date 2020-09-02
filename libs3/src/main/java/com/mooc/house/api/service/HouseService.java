package com.mooc.house.api.service;

import com.mooc.house.api.common.CommonConstants;
import com.mooc.house.api.common.PageData;
import com.mooc.house.api.common.PageParams;
import com.mooc.house.api.dao.common.RestResponse;
import com.mooc.house.api.dao.house.HouseClient;
import com.mooc.house.api.dao.house.model.City;
import com.mooc.house.api.dao.house.model.Community;
import com.mooc.house.api.dao.house.model.House;
import com.mooc.house.api.dao.house.model.HouseQueryRequest;
import com.mooc.house.api.dao.house.model.HouseUserRequest;
import com.mooc.house.api.dao.house.model.ListResponse;
import com.mooc.house.api.dao.house.model.UserMsg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * recommend houses
 * query areas
 * query houses
 * update house rating
 * add user message corresponding to one house
 * bind user with one house
 */

@Service
public class HouseService {

    // latest houses that will be presented on home page
    private static final int NUM_LATEST_HOUSES = 8;

    @Autowired
    private HouseClient houseClient;

    /**
     *
     * recommend
     * latest houses
     * hottest houses
     *
     */

    // query latest houses
    public List<House> getLatest(){

        // get latest houses rest response
        RestResponse<List<House>> restResponse = houseClient.getLatestHouses(NUM_LATEST_HOUSES);

        // get latest houses
        List<House> houses = restResponse.getResult();

        return houses;
    }

    // get hot houses to recommend
    public List<House> getHotHouses(Integer recomSize){

        // get hot houses rest response
        RestResponse<List<House>> restResponse = houseClient.getHotHouses(recomSize);

        // get hot houses
        List<House> houses = restResponse.getResult();

        return houses;
    }


    /**
     *
     * query area
     * cities
     * communities
     */
    public List<City> getAllCities(){

        // get cities rest response
        RestResponse<List<City>> restResponse = houseClient.getAllCities();

        // get cities
        List<City> cities = restResponse.getResult();

        return cities;
    }


    public List<Community> getAllCommunities(){

        // get communities rest response
        RestResponse<List<Community>> restResponse = houseClient.getAllCommunities();

        // get communities
        List<Community> communities = restResponse.getResult();

        return communities;
    }


    /**
     *
     * query house
     * query house by id
     * query house by params
     */

    // query one house by id
    public House queryOneHouse(long id){

        // get house  rest response by id
        RestResponse<House> restResponse = houseClient.getHouseDetail(id);

        // get house
        House house = restResponse.getResult();

        return house;
    }


    // query houses by params and limited by page params
    public PageData<House> queryHouse(House query, PageParams build){

        // house query request params
        HouseQueryRequest houseQueryRequest = new HouseQueryRequest();
        houseQueryRequest.setHouse(query);
        houseQueryRequest.setLimit(build.getLimit());
        houseQueryRequest.setOffset(build.getOffset());

        // get house data rest response
        RestResponse<ListResponse<House>> restResponse = houseClient.getHouses(houseQueryRequest);

        // get house data list response
        ListResponse<House> listResponse = restResponse.getResult();

        // get data
        List<House> houses = listResponse.getList();
        long count = listResponse.getCount();

        return PageData.buildPage(houses, count, build.getPageSize(), build.getPageNum());
    }


    /**
     *
     * update house rating
     *
     */
    public void updateRating(Long id, Double rating){

        // house rating request params
        HouseQueryRequest houseQueryRequest = new HouseQueryRequest();

        House house = new House();
        house.setId(id);
        house.setRating(rating);
        houseQueryRequest.setHouse(house);

        // update house rating
        RestResponse<Object> updateHouseRating =  houseClient.updateHouseRating(houseQueryRequest);
    }


    /**
     *
     * add user message
     *
     */
    public void addUserMsg(UserMsg userMsg){

        // update user message
        RestResponse<Object> restResponse = houseClient.insertHouseMessage(userMsg);
    }


    /**
     *
     *
     * bind user to house
     * bind
     * unbind
     */

    // bind user to house
    public void bindUserToHouse(Long houseId, Long userId, boolean bookmark){

        // house user request params
        HouseUserRequest houseUserRequest = new HouseUserRequest();

        houseUserRequest.setHouseId(houseId);
        houseUserRequest.setUserId(userId);
        houseUserRequest.setBindType(bookmark? CommonConstants.BOOKMARK : CommonConstants.SALE);

        // bind user to house
        RestResponse<Object> restResponse = houseClient.bind(houseUserRequest);
    }

    // unbind user to house
    public void unbindUserToHouse(Long houseId, Long userId, boolean bookmark){

        // house user request params
        HouseUserRequest houseUserRequest = new HouseUserRequest();

        houseUserRequest.setHouseId(houseId);
        houseUserRequest.setUserId(userId);
        houseUserRequest.setBindType(bookmark? CommonConstants.BOOKMARK : CommonConstants.SALE);
        houseUserRequest.setUnBind(true);

        // bind user to house
        RestResponse<Object> restResponse = houseClient.bind(houseUserRequest);

    }


}
