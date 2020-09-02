package com.mooc.house.comment.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mooc.house.comment.common.LimitOffset;
import com.mooc.house.comment.mapper.BlogMapper;
import com.mooc.house.comment.model.Blog;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService implements IBlogService{

    @Autowired
    private BlogMapper blogMapper;

    /**
     * base
     */
    public List<Blog> selectBlog(Blog blog, LimitOffset limitOffset){
        return blogMapper.selectBlog(blog, limitOffset);
    }

    public Long getNumberBlog(){
        return blogMapper.getNumberBlog();
    }

    /**
     * service
     */
    @Override
    public Blog getBlogById(Integer id){

        // query params
        Blog query = new Blog();
        query.setId(id);

        // get the the first blog if find many
        List<Blog> blogs = selectBlog(query, LimitOffset.build(1,0));
        if(!blogs.isEmpty()){
            return blogs.get(0);
        }
        return null;
    }

    @Override
    public Pair<List<Blog>, Long> queryBlog(Blog blog, Integer limit, Integer offset){

        // blog list for current page
        List<Blog> blogList = selectBlog(blog, LimitOffset.build(limit, offset));

        // populate
        populate(blogList);

        // total count
        Long count = getNumberBlog();

        // return a pair of blog list and total count
        return ImmutablePair.of(blogList, count);
    }

    // abstract blog content
    private void populate(List<Blog> blogList){
        if(blogList.isEmpty()){
            return;
        }

        for(Blog blog : blogList){

            // set blog's digest
            String content = blog.getContent();
            Document document = Jsoup.parse(content);
            String text = document.text();
            blog.setDigest(text.substring(0, Math.min(text.length(), 40)));

            // split blog's tags
            String tags = blog.getTags();
            blog.setTagList(Lists.newArrayList(Splitter.on(",").split(tags)));
        }
    }


}
