package com.mooc.house.user.controller;

import com.mooc.house.user.common.PageParams;
import com.mooc.house.user.common.RestResponse;
import com.mooc.house.user.model.ListResponse;
import com.mooc.house.user.model.User;
import com.mooc.house.user.service.AgencyService;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agents")
public class AgentController {

    @Autowired
    private AgencyService agencyService;

    @GetMapping("/list")
    public RestResponse<ListResponse<User>> getAgentsByLimit(@RequestParam("limit") Integer limit, @RequestParam("offset") Integer offset){
        PageParams pageParams = new PageParams();
        pageParams.setLimit(limit);
        pageParams.setOffset(offset);
        Pair<List<User>, Long> pair = agencyService.getAgentsByLimit(pageParams);
        ListResponse<User> listResponse = ListResponse.build(pair.getLeft(), pair.getRight());
        return RestResponse.success(listResponse);
    }

    @GetMapping("/{id}")
    public RestResponse<User> getAgentDetail(@PathVariable("id") Long id){
        User user = agencyService.getAgentDetail(id);
        return RestResponse.success(user);
    }

}
