package com.mooc.house.user.test;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonMapper {

    Person selectById(int id);


}
