package com.mooc.house.hsrv.service;

import com.mooc.house.hsrv.mapper.CommunityMapper;
import com.mooc.house.hsrv.model.Community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityService {

    @Autowired
    private CommunityMapper communityMapper;

    /**
     * community
     *
     */
    public List<Community> getAllCommunities(){
        Community community = new Community();
        return communityMapper.selectCommunities(community);
    }


}
