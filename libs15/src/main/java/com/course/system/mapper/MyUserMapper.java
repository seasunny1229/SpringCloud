package com.course.system.mapper;

import com.course.system.dto.ResourceDto;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyUserMapper {

    List<ResourceDto> findResources(@Param("userId") String userId);

}
