package com.mooc.house.hsrv.mapper;

import com.mooc.house.hsrv.model.Community;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityMapper {

    /**
     * Community
     */
    // select all communities or select communities by condition
    List<Community> selectCommunities(Community community);

}
