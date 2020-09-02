package com.mooc.house.comment.mapper;

import com.mooc.house.comment.model.Comment;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    int insert(@Param("comment") Comment comment);

    List<Comment> selectComments(@Param("house_id") Long houseId, @Param("size") int size);

    List<Comment> selectBlogComments(@Param("blog_id") Long blogId, @Param("size") int size);

}
