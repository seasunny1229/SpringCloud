package com.mooc.house.user.mapper;

import com.mooc.house.user.model.Agency;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AgencyMapper {

    int insert(Agency agency);

    List<Agency> select(Agency agency);
}
