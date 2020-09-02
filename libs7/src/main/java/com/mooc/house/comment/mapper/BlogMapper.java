package com.mooc.house.comment.mapper;

import com.mooc.house.comment.common.LimitOffset;
import com.mooc.house.comment.model.Blog;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper {

    List<Blog> selectBlog(@Param("blog") Blog blog, @Param("pageParams")LimitOffset limitOffset);

    Long getNumberBlog();
}
