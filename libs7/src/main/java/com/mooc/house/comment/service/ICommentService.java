package com.mooc.house.comment.service;

import com.mooc.house.comment.model.Comment;

import java.util.List;

public interface ICommentService {

    List<Comment> getHouseComments(Long houseId, Integer size);

    List<Comment> getBlogComments(Long blogId, Integer size);

    void addHouseComment(Long houseId, String content, Long userId);

    void addBlogComment(Long blogId, String content, Long userId);

}
