package com.mooc.house.comment.test;

import com.mooc.house.comment.common.LimitOffset;
import com.mooc.house.comment.model.Blog;
import com.mooc.house.comment.model.Comment;
import com.mooc.house.comment.service.BlogService;
import com.mooc.house.comment.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String test(){
        return "hello user";
    }

    @GetMapping("/blog")
    public List<Blog> getAllBlog(){
        Blog blog = new Blog();
        LimitOffset limitOffset =LimitOffset.build(3,1);
        return blogService.selectBlog(blog, limitOffset);
    }

    @GetMapping("/blog/count")
    public Long getBlog(){
        return blogService.getNumberBlog();
    }

    @GetMapping("/comment/insert")
    public Integer insertComment(){
        Comment comment = new Comment();
        comment.setBlogId(0L);
        comment.setContent("hello world");
        comment.setCreateTime(new Date());
        comment.setHouseId(5L);
        comment.setUserId(7L);
        return commentService.insert(comment);
    }

    @GetMapping("/comment/house")
    public List<Comment> getHouseComments(){
        return commentService.selectComments(5L,1);
    }

    @GetMapping("/comment/blog")
    public List<Comment> getBlogComments(){
        return commentService.selectBlogComments(0L,3);
    }

}


