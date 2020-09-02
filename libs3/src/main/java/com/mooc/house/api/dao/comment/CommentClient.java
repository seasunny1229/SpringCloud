package com.mooc.house.api.dao.comment;

import com.mooc.house.api.dao.comment.model.Blog;
import com.mooc.house.api.dao.comment.model.BlogQueryRequest;
import com.mooc.house.api.dao.comment.model.Comment;
import com.mooc.house.api.dao.comment.model.CommentQueryRequest;
import com.mooc.house.api.dao.comment.model.ListResponse;
import com.mooc.house.api.dao.common.RestResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("comment")
public interface CommentClient {

    @PostMapping(value = "blog")
    RestResponse<ListResponse<Blog>> getBlogList(@RequestBody BlogQueryRequest blogQueryRequest);

    @PostMapping(value = "comments/all")
    RestResponse<List<Comment>> getAllComments(@RequestBody CommentQueryRequest commentQueryRequest);

    @PostMapping(value = "comments")
    RestResponse<Object> insertComment(@RequestBody CommentQueryRequest commentQueryRequest);
}
