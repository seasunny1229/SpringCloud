package com.mooc.house.comment.service;

import com.mooc.house.comment.model.Blog;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IBlogService {

    Blog getBlogById(Integer id);

    Pair<List<Blog>, Long> queryBlog(Blog blog, Integer limit, Integer offset);

}
