package com.mooc.house.comment.controller;

import com.mooc.house.comment.common.RestResponse;
import com.mooc.house.comment.model.Blog;
import com.mooc.house.comment.model.BlogQueryRequest;
import com.mooc.house.comment.model.ListResponse;
import com.mooc.house.comment.service.IBlogService;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    IBlogService blogService;

    // get one blog by id
    @GetMapping("/{id}")
    public RestResponse<Blog> getBlogById(@PathVariable("id") Integer id){
        Blog blog = blogService.getBlogById(id);
        return RestResponse.success(blog);
    }

    // get a blog list queried by blog params and limited by page params
    @PostMapping
    public RestResponse<ListResponse<Blog>> getBlogList(@RequestBody BlogQueryRequest blogQueryRequest){
        Pair<List<Blog>, Long> pair = blogService.queryBlog(blogQueryRequest.getBlog(), blogQueryRequest.getLimit(), blogQueryRequest.getOffset());
        ListResponse<Blog> listResponse = ListResponse.build(pair.getLeft(), pair.getRight());
        return RestResponse.success(listResponse);
    }

}
