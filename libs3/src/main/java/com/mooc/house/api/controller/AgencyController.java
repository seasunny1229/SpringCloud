package com.mooc.house.api.controller;


import com.mooc.house.api.common.CommonConstants;
import com.mooc.house.api.common.PageData;
import com.mooc.house.api.common.PageParams;
import com.mooc.house.api.common.ResultMsg;
import com.mooc.house.api.common.UserContext;
import com.mooc.house.api.dao.house.model.House;
import com.mooc.house.api.dao.user.model.Agency;
import com.mooc.house.api.dao.user.model.User;
import com.mooc.house.api.service.AgencyService;
import com.mooc.house.api.service.HouseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

/**
 *
 * agency
 * agent
 * leave message to agent
 *
 */


@Controller
public class AgencyController {


    @Autowired
    private AgencyService agencyService;

    @Autowired
    private HouseService houseService;


    /**
     *
     * agency
     * create agency
     * query agency list
     * query agency detail
     *
     */

    // create agency
    @RequestMapping("/agency/create")
    public String agencyCreate(){

        // user
        User user = UserContext.getUser();

        // login first if user is null
        if(user == null){
            return "redirect:/accounts/signin?" + ResultMsg.errorMsg("login in first").asUrlParams();
        }

        return "/user/agency/create";

    }


    // submit agency info
    @RequestMapping("/agency/submit")
    public String agencySubmit(Agency agency){

        // user
        User user = UserContext.getUser();

        // login first if user is null
        if(user == null){
            return "redirect:/accounts/signin?" + ResultMsg.errorMsg("login in first").asUrlParams();
        }


        // insert agency
        agencyService.addAgency(agency);

        return "redirect:/index?" + ResultMsg.errorMsg("success").asUrlParams();
    }


    // list agencies
    @RequestMapping("/agency/list")
    public String agencyList(ModelMap modelMap){

        // get agency data
        List<Agency> agencies = agencyService.getAllAgencies();

        // hot houses
        List<House> houses = houseService.getHotHouses(CommonConstants.RECOM_SIZE);

        // model and map
        modelMap.put("recomHouses", houses);
        modelMap.put("agencyList", agencies);

        return "/user/agency/agencyList";
    }


    // present agent detail
    @RequestMapping("/agency/agencyDetail")
    public String agencyDetail(Integer id, ModelMap modelMap){

        // get agency detail
        Agency agency = agencyService.getAgencyById(id);

        // hot houses
        List<House> houses = houseService.getHotHouses(CommonConstants.RECOM_SIZE);

        // model and map
        modelMap.put("recomHouses", houses);
        modelMap.put("agency", agency);

        return "/user/agency/agencyDetail";
    }


    /**
     *
     * agent
     * list all agents
     * present agent detail
     *
     */

    // list all agents
    @RequestMapping("/agency/agentList")
    public String agentList(Integer pageSize, Integer pageNum, ModelMap modelMap){

        // set default params
        if(pageSize == null){
            pageSize = CommonConstants.NUM_DEFAULT_AGENTS;
        }

        // agent page data
        PageData<User> ps = agencyService.getAllAgents(PageParams.build(pageSize, pageNum));

        // hot houses
        List<House> houses = houseService.getHotHouses(CommonConstants.RECOM_SIZE);


        // model and map
        modelMap.put("recomHouses", houses);
        modelMap.put("ps", ps);

        return "/user/agent/agentList";
    }


    // present agent detail
    @RequestMapping("/agency/agentDetail")
    public String agentDetail(Long id, ModelMap modelMap){

        // get agent
        User user = agencyService.getAgentDetail(id);

        // hot houses
        List<House> houses = houseService.getHotHouses(CommonConstants.RECOM_SIZE);

        // query houses that belongs to this agent
        House query = new House();
        query.setUserId(id);
        query.setBookmarked(false);
        PageParams pageParams = PageParams.build(3,1);
        PageData<House> bindHouses = houseService.queryHouse(query, pageParams);

        // model map
        if(bindHouses != null){
            modelMap.put("bindHouses", bindHouses.getList());
        }
        modelMap.put("recomHouses", houses);
        modelMap.put("agent", user);

        return "/user/agent/agentDetail";
    }


    /**
     *
     * leave message to agent
     *
     *
     */
    @RequestMapping("/agency/agentMsg")
    public String agentMsg(Long id, String msg, String name, String email, ModelMap modelMap){


        // get agent
        User user = agencyService.getAgentDetail(id);

        // send email


        return "redirect:/agency/agentDetail?id=" + id + "&" + ResultMsg.successMsg("success").asUrlParams();
    }




}
