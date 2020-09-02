package com.mooc.house.user.service;

import com.mooc.house.user.common.PageParams;
import com.mooc.house.user.mapper.AgencyMapper;
import com.mooc.house.user.mapper.UserMapper;
import com.mooc.house.user.model.Agency;
import com.mooc.house.user.model.User;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgencyService {

    @Autowired
    private AgencyMapper agencyMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * agency
     */
    public List<Agency> getAllAgencies(){
        Agency agency = new Agency();
        return agencyMapper.select(agency);
    }

    public Agency getAgencyById(Integer id){
        Agency agency = new Agency();
        agency.setId(id);
        List<Agency> agencies = agencyMapper.select(agency);
        if(agencies.isEmpty()){
            return null;
        }
        else {
            return agencies.get(0);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int addAgency(Agency agency){
        return agencyMapper.insert(agency);
    }

    /**
     * agent
     */
    public Pair<List<User>, Long> getAgentsByLimit(PageParams pageParams){
        User user = new User();
        user.setType(User.TYPE_AGENT);
        List<User> agents = userMapper.selectByLimit(user, pageParams);
        setImg(agents);
        Long count = userMapper.getUserCount(new User());
        return ImmutablePair.of(agents, count);
    }

    public User getAgentDetail(Long id){
        User user = new User();
        user.setId(id);
        user.setType(User.TYPE_AGENT);
        List<User> users = userMapper.select(user);
        setImg(users);
        if(!users.isEmpty()){

            // agent
            User agent = users.get(0);

            // agency
            Agency agency = new Agency();
            agency.setId(agent.getAgencyId());
            List<Agency> agencies = agencyMapper.select(agency);
            if(!agencies.isEmpty()){
                agent.setAgentName(agencies.get(0).getName());
            }

            return agent;
        }

        return null;
    }

    private void setImg(List<User> users){

    }

}
