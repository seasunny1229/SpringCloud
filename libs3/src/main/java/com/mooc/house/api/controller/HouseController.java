package com.mooc.house.api.controller;

import com.mooc.house.api.common.CommonConstants;
import com.mooc.house.api.common.PageData;
import com.mooc.house.api.common.PageParams;
import com.mooc.house.api.common.ResultMsg;
import com.mooc.house.api.common.UserContext;
import com.mooc.house.api.dao.comment.model.Comment;
import com.mooc.house.api.dao.house.model.House;
import com.mooc.house.api.dao.house.model.UserMsg;
import com.mooc.house.api.dao.user.model.User;
import com.mooc.house.api.service.AgencyService;
import com.mooc.house.api.service.CommentService;
import com.mooc.house.api.service.HouseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;

@Controller
public class HouseController {


    @Autowired
    private HouseService houseService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AgencyService agencyService;


    /**
     * bookmark
     *
     */
    // bookmark to house
    @RequestMapping(value = "/house/bookmark", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResultMsg bookmark(Long id, ModelMap modelMap){

        // get user
        User user = UserContext.getUser();

        // bind user to house
        houseService.bindUserToHouse(id, user.getId(), true);

        return ResultMsg.success();
    }

    // unbookmark from  house
    @RequestMapping(value = "/house/unbookmark", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResultMsg unbookmark(Long id, ModelMap modelMap){

        // get user
        User user = UserContext.getUser();

        // unbind user to house
        houseService.unbindUserToHouse(id, user.getId(), true);

        return ResultMsg.success();
    }


    /**
     *
     * house rating
     *
     */
    @RequestMapping(value = "/house/rating", method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ResultMsg houseRate(Double rating, Long id){

        // update house rating
        houseService.updateRating(id, rating);

        return ResultMsg.success();
    }

    /**
     *
     * house messages
     *
     *
     */
    @RequestMapping(value = "/house/leaveMsg", method = {RequestMethod.POST,RequestMethod.GET})
    public String houseMsg(UserMsg userMsg){

        // leave message
        houseService.addUserMsg(userMsg);

        return "redirect:/house/detail?id=" + userMsg.getHouseId() + "&" + ResultMsg.successMsg("success leaving a message").asUrlParams();

    }



    /**
     *  owner house list
     *
     */
    // list booked houses of one user
    @RequestMapping(value = "/house/bookmarked", method = {RequestMethod.GET, RequestMethod.POST})
    public String bookmarked(House house, PageParams pageParams, ModelMap modelMap){

        // user
        User user = UserContext.getUser();

        // house query params
        house.setBookmarked(true);
        house.setUserId(user.getId());

        // query houses
        PageData<House> houses = houseService.queryHouse(house, pageParams);

        // model and map
        modelMap.put("ps", houses);
        modelMap.put("pageType", "book");

        return "/house/ownlist";
    }


    @RequestMapping(value = "/house/del", method = {RequestMethod.POST, RequestMethod.GET})
    public String delsale(Long id, String pageType){

        // get user
        User user = UserContext.getUser();

        // unbind user to house
        houseService.unbindUserToHouse(id, user.getId(), !pageType.equals("own"));

        return "redirect:/house/ownlist";
    }

    @RequestMapping(value = "/house/ownlist", method = {RequestMethod.POST, RequestMethod.GET})
    public String ownlist(House house, PageParams pageParams, ModelMap modelMap){

        // user
        User user = UserContext.getUser();

        // house query params
        house.setUserId(user.getId());
        house.setBookmarked(false);

        // query house
        PageData<House> housePageData = houseService.queryHouse(house, pageParams);

        // model map
        modelMap.put("ps",  housePageData);
        modelMap.put("pageType", "own");

        return "/house/ownlist";
    }


    /**
     *
     *  house list
     *  house detail
     *
     */
    @RequestMapping(value = "/house/list", method={RequestMethod.POST,RequestMethod.GET})
    public String houseList(Integer pageSize, Integer pageNum, House query, ModelMap modelMap){

        // query houses
        PageData<House> ps = houseService.queryHouse(query, PageParams.build(pageSize, pageNum));

        // hottest houses
        List<House> rcHouses = houseService.getHotHouses(CommonConstants.RECOM_SIZE);

        // model and map
        modelMap.put("recomHouses", rcHouses);
        modelMap.put("vo", query);
        modelMap.put("ps", ps);

        return "/house/listing";
    }

    @RequestMapping(value = "/house/detail", method={RequestMethod.POST,RequestMethod.GET})
    public String houseDetail(long id, ModelMap modelMap){

        // get one house by id
        House house = houseService.queryOneHouse(id);

        // comments
        List<Comment> comments = commentService.getHouseComments(id);

        // recommend houses
        List<House> rcHouses = houseService.getHotHouses(CommonConstants.RECOM_SIZE);

        if(house.getUserId() != null){
            if(!Objects.equals(0L, house.getUserId())){

                // get agent
               User agent = agencyService.getAgentDetail(house.getUserId());

                // model and map
                modelMap.put("agent", agent);
            }
        }

        // model and map
        modelMap.put("house", house);
        modelMap.put("recomHouses", rcHouses);
        modelMap.put("commentList", comments);

        return "/house/detail";
    }

}
