package com.mooc.house.comment.controller;

import com.mooc.house.comment.common.RestResponse;
import com.mooc.house.comment.model.Comment;
import com.mooc.house.comment.model.CommentQueryRequest;
import com.mooc.house.comment.service.ICommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    // get a comment list queried by comment params and limited by size
    @PostMapping("/all")
    public RestResponse<List<Comment>> getAllComments(@RequestBody CommentQueryRequest commentQueryRequest){

        // type refers to comments about blog or house
        Integer type = commentQueryRequest.getType();

        // get comments
        List<Comment> comments = null;
        if(type == Comment.TYPE_HOUSE){
            comments = commentService.getHouseComments(commentQueryRequest.getHouseId(), commentQueryRequest.getSize());
        }
        else {
            comments = commentService.getBlogComments(commentQueryRequest.getBlogId(), commentQueryRequest.getSize());
        }

        return RestResponse.success(comments);
    }

    // insert a comment
    @PostMapping
    public RestResponse<Object> insertComment(@RequestBody CommentQueryRequest commentQueryRequest){
        Integer type = commentQueryRequest.getType();
        if(type == Comment.TYPE_HOUSE){
            commentService.addHouseComment(commentQueryRequest.getHouseId(), commentQueryRequest.getContent(), commentQueryRequest.getUserId());
        }
        else {
            commentService.addBlogComment(commentQueryRequest.getBlogId(), commentQueryRequest.getContent(), commentQueryRequest.getUserId());
        }
        return RestResponse.success();
    }

}
