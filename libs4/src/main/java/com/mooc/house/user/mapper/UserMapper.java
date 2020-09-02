package com.mooc.house.user.mapper;

import com.mooc.house.user.common.PageParams;
import com.mooc.house.user.model.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User account);

    int delete(String email);

    int update(User user);

    List<User> select(User user);

    List<User> selectByLimit(@Param("user") User user, @Param("page_params") PageParams pageParams);

    List<User> selectById(Long id);

    List<User> selectByEmail(String email);

    Long getUserCount(User user);

}
