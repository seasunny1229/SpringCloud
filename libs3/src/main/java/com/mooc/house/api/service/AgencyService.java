package com.mooc.house.api.service;

import com.mooc.house.api.common.PageData;
import com.mooc.house.api.common.PageParams;
import com.mooc.house.api.dao.common.RestResponse;
import com.mooc.house.api.dao.user.TestUserClient;
import com.mooc.house.api.dao.user.model.Agency;
import com.mooc.house.api.dao.user.model.ListResponse;
import com.mooc.house.api.dao.user.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * agency
 * query agency
 * add agency
 * query agents
 */
@Service
public class AgencyService {


    @Autowired
    private TestUserClient userClient;


    /**
     *
     * query agency
     * query agency by id
     * query all agencies
     *
     */
    public Agency getAgencyById(int id){

        // rest response
        RestResponse<Agency> restResponse = userClient.getAgencyById(id);

        // agency
        Agency agency = restResponse.getResult();

        return agency;
    }

    public List<Agency> getAllAgencies(){

        // rest response
        RestResponse<List<Agency>> restResponse =  userClient.getAllAgencies();

        // agencies
        List<Agency> agencies = restResponse.getResult();

        return agencies;
    }

    /**
     * add agency
     *
     */
    public void addAgency(Agency agency){

        // add agency
        RestResponse<Object> restResponse = userClient.addAgency(agency);
    }


    /**
     *
     * agents
     * get all agents
     * get agent by id
     */
    public PageData<User> getAllAgents(PageParams pageParams){

        // rest response
        RestResponse<ListResponse<User>> restResponse = userClient.getAgentsByLimit(pageParams.getLimit(), pageParams.getOffset());

        // list response
        ListResponse<User> listResponse = restResponse.getResult();

        // data
        List<User> agents = listResponse.getList();
        long count = listResponse.getCount();

        return PageData.buildPage(agents, count, pageParams.getPageSize(), pageParams.getPageNum());
    }

    public User getAgentDetail(Long id){

        // rest response
        RestResponse<User> restResponse = userClient.getAgentDetail(id);

        User agent = restResponse.getResult();

        return agent;
    }

}
