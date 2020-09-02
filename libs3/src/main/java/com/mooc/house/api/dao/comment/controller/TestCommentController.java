package com.mooc.house.api.dao.comment.controller;

import com.mooc.house.api.dao.comment.CommentClient;
import com.mooc.house.api.dao.comment.model.Blog;
import com.mooc.house.api.dao.comment.model.BlogQueryRequest;
import com.mooc.house.api.dao.comment.model.Comment;
import com.mooc.house.api.dao.comment.model.CommentQueryRequest;
import com.mooc.house.api.dao.comment.model.ListResponse;
import com.mooc.house.api.dao.common.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class TestCommentController {

    @Autowired
    private CommentClient commentClient;

    @GetMapping("/test")
    public String test(){
        System.out.println("comment test");
        BlogQueryRequest blogQueryRequest = new BlogQueryRequest();
        blogQueryRequest.setBlog(new Blog());
        blogQueryRequest.setLimit(1);
        blogQueryRequest.setOffset(0);
        System.out.println("test comment blogs in libs3  " + blogQueryRequest);
        return "comment test";
    }

    @GetMapping("/blogs")
    public RestResponse<ListResponse<Blog>> getBlogList(){
        BlogQueryRequest blogQueryRequest = new BlogQueryRequest();
        blogQueryRequest.setBlog(new Blog());
        blogQueryRequest.setLimit(4);
        blogQueryRequest.setOffset(0);
        return commentClient.getBlogList(blogQueryRequest);
    }

    @GetMapping("/comments/house")
    public RestResponse<List<Comment>> getHouseCommentList(){
        CommentQueryRequest commentQueryRequest = new CommentQueryRequest();
        commentQueryRequest.setType(Comment.TYPE_HOUSE);
        commentQueryRequest.setHouseId(5L);
        commentQueryRequest.setSize(3);
        return commentClient.getAllComments(commentQueryRequest);
    }

    @GetMapping("/comments/comment")
    public RestResponse<List<Comment>> getCommentCommentList(){
        CommentQueryRequest commentQueryRequest = new CommentQueryRequest();
        commentQueryRequest.setType(Comment.TYPE_COMMENT);
        commentQueryRequest.setBlogId(1L);
        commentQueryRequest.setSize(5);
        return commentClient.getAllComments(commentQueryRequest);
    }

    @GetMapping("/comments/inserthouse")
    public RestResponse<Object> insertHouseComment(){
        CommentQueryRequest commentQueryRequest = new CommentQueryRequest();
        commentQueryRequest.setHouseId(5L);
        commentQueryRequest.setUserId(7L);
        commentQueryRequest.setType(Comment.TYPE_HOUSE);
        commentQueryRequest.setContent("just for test use house");
        return commentClient.insertComment(commentQueryRequest);
    }

    @GetMapping("/comments/insertblog")
    public RestResponse<Object> insertBlogComment(){
        CommentQueryRequest commentQueryRequest = new CommentQueryRequest();
        commentQueryRequest.setBlogId(2L);
        commentQueryRequest.setUserId(7L);
        commentQueryRequest.setType(Comment.TYPE_COMMENT);
        commentQueryRequest.setContent("just for test use blog");
        return commentClient.insertComment(commentQueryRequest);
    }
}
