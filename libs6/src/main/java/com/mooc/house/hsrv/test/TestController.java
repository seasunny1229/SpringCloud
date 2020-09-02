package com.mooc.house.hsrv.test;

import com.google.common.collect.Lists;
import com.mooc.house.hsrv.model.City;
import com.mooc.house.hsrv.model.Community;
import com.mooc.house.hsrv.model.House;
import com.mooc.house.hsrv.model.HouseUser;
import com.mooc.house.hsrv.model.UserMsg;
import com.mooc.house.hsrv.service.CityService;
import com.mooc.house.hsrv.service.CommunityService;
import com.mooc.house.hsrv.service.HouseService;
import com.mooc.house.hsrv.service.HouseUserService;
import com.mooc.house.hsrv.service.UserMsgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private CityService cityService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserMsgService userMsgService;

    @Autowired
    private HouseUserService houseUserService;

    @GetMapping
    public String test(){
        return "hello user";
    }

    @GetMapping("/cities")
    public List<City> getAllCities(){
        return cityService.getAllCities();
    }

    @GetMapping("/communities")
    public List<Community> getAllCommunities(){
        return communityService.getAllCommunities();
    }

    @GetMapping("/houses/all")
    public List<House> getAllHouses(){
        return houseService.getAllHouses();
    }

    @GetMapping("/houses/{id}")
    public List<House> getHouseById(@PathVariable("id") Long id){
        return houseService.getHouseById(id);
    }

    @GetMapping("/houses/pages")
    public List<House> getHouseByLimit(
            @RequestParam("limit") int limit,
            @RequestParam("offset") int offset,
            @RequestParam("order")String order){
        return houseService.getHouseByLimit(limit, offset, order);
    }

    @GetMapping("/houses/count")
    public Long getHouseCount(){
        House house = new House();
        house.setIds(Lists.<Long>newArrayList(22L,23L));
        return houseService.getNumberHouses(house);
    }

    @GetMapping("/houses/update")
    public int updateHouse(){
        House house = new House();
        house.setRating(1.2);
        house.setId(25L);
        return houseService.updateHouse(house);
    }


    @GetMapping("/messages/insert")
    public int insertHouseMessage(){
        UserMsg userMsg = new UserMsg();
        userMsg.setAgentId(7L);
        userMsg.setCreateTime(new Date());
        userMsg.setUserName("12");
        userMsg.setHouseId(24L);
        userMsg.setMsg("hello");
        return userMsgService.insertUserMsg(userMsg);
    }


    @GetMapping("/houseusers/insert")
    public int insertHouseUser(){
        HouseUser houseUser = new HouseUser();
        houseUser.setHouseId(28L);
        houseUser.setUserId(38L);
        houseUser.setCreateTime(new Date());
        houseUser.setType(1);
        return houseUserService.insertHouseUser(houseUser);
    }

    @GetMapping("/houseusers/delete")
    public int deleteHouseUser(){
        return houseUserService.deleteHouseUser(28L, 38L, 1);
    }


    @GetMapping("/houseusers/select")
    public List<HouseUser> selectHouseUsers(){
        return houseUserService.selectHouseUsers(27L, 38L, 1);
    }

    @GetMapping("/houseusers/selectbyhouseid")
    public List<HouseUser> selectHouseUsersById(){
        return houseUserService.selectHouseUsersByHouseId(24L, 2);
    }


}
