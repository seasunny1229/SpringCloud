package com.mooc.house.hsrv.service;

import com.mooc.house.hsrv.common.HouseUserType;
import com.mooc.house.hsrv.mapper.HouseMapper;
import com.mooc.house.hsrv.mapper.HouseUserMapper;
import com.mooc.house.hsrv.model.House;
import com.mooc.house.hsrv.model.HouseUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HouseUserService {

    @Autowired
    private HouseUserMapper houseUserMapper;

    @Autowired
    private HouseMapper houseMapper;

    /**
     * base
     */
    public int insertHouseUser(HouseUser houseUser){
        return houseUserMapper.insertHouseUser(houseUser);
    }

    public int deleteHouseUser(Long houseId, Long userId, int type){
        return houseUserMapper.deleteHouseUser(houseId, userId, type);
    }

    public List<HouseUser> selectHouseUsers(Long houseId, Long userId, int type){
        return houseUserMapper.selectHouseUsers(houseId, userId, type);
    }

    public List<HouseUser> selectHouseUsersByHouseId(Long houseId, int type){
        return houseUserMapper.selectHouseUsersByHouseId(houseId, type);
    }

    /**
     * service
     */
    public int bindUserToHouse(Long houseId, Long userId, HouseUserType houseUserType){

        // get existed house user
        List<HouseUser> existedHouseUser = houseUserMapper.selectHouseUsers(houseId, userId, houseUserType.value);

        // do not change existed house user
        if(!existedHouseUser.isEmpty()){
            return 0;
        }

        // bind
        HouseUser houseUser = new HouseUser();
        houseUser.setHouseId(houseId);
        houseUser.setUserId(userId);
        houseUser.setType(houseUserType.value);
        houseUser.setCreateTime(new Date());

        // insert
        return houseUserMapper.insertHouseUser(houseUser);
    }

    public int unbindUserToHouse(Long houseId, Long userId, HouseUserType houseUserType){
        int result;
        if(houseUserType.equals(HouseUserType.SALE)){
            House house = new House();
            house.setId(houseId);
            house.setState(House.STATE_DOWN);
            result = houseMapper.updateHouse(house);
        }
        else {
            result = houseUserMapper.deleteHouseUser(houseId, userId, houseUserType.value);
        }
        return result;
    }

}
