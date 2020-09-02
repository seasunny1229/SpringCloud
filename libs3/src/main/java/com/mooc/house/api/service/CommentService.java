package com.mooc.house.api.service;

import com.mooc.house.api.common.PageData;
import com.mooc.house.api.common.PageParams;
import com.mooc.house.api.dao.comment.CommentClient;
import com.mooc.house.api.dao.comment.model.Blog;
import com.mooc.house.api.dao.comment.model.BlogQueryRequest;
import com.mooc.house.api.dao.comment.model.Comment;
import com.mooc.house.api.dao.comment.model.CommentQueryRequest;
import com.mooc.house.api.dao.comment.model.ListResponse;
import com.mooc.house.api.dao.common.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * query blog
 * query comments
 * insert comments
 */

@Service
public class CommentService {

    private static final int NUM_BLOG_COMMENTS = 8, NUM_HOUSE_COMMENTS = 8;


    @Autowired
    private CommentClient commentClient;


    /**
     * blog
     * query blog list in a page
     * query blog detail
     *
     */

    // query one blog by id
    public Blog queryOneBlog(int id){

        // blog detail query request params
        BlogQueryRequest blogQueryRequest = new BlogQueryRequest();

        // query blog params
        Blog queryBlog = new Blog();
        queryBlog.setId(id);

        // query request params
        blogQueryRequest.setBlog(queryBlog);
        blogQueryRequest.setLimit(1);
        blogQueryRequest.setOffset(0);

        // get blog data rest response
        RestResponse<ListResponse<Blog>> responseRestResponse = commentClient.getBlogList(blogQueryRequest);

        // get blog data list response
        ListResponse<Blog> listResponse = responseRestResponse.getResult();

        // get blog data
        List<Blog> blogList = listResponse.getList();

        return blogList.get(0);

    }

    // query a blog list by blog params and limited by page params
    public PageData<Blog> queryBlogs(Blog query, PageParams build){

        // blog list query request params
        BlogQueryRequest blogQueryRequest = new BlogQueryRequest();
        blogQueryRequest.setBlog(query);
        blogQueryRequest.setLimit(build.getLimit());
        blogQueryRequest.setOffset(build.getOffset());

        // get blog data rest response
        RestResponse<ListResponse<Blog>> responseRestResponse = commentClient.getBlogList(blogQueryRequest);

        // get blog data list response
        ListResponse<Blog> listResponse = responseRestResponse.getResult();

        // get blog data
        List<Blog> blogList = listResponse.getList();
        Long count = listResponse.getCount();

        return PageData.buildPage(blogList, count, build.getPageSize(), build.getPageNum());
    }


    /**
     * query comments
     * query blog comments
     * query house comments
     */

    // query blog comments
    public List<Comment> getBlogComments(int id){

        // blog comment query request params
        CommentQueryRequest commentQueryRequest = new CommentQueryRequest();
        commentQueryRequest.setBlogId(Long.getLong(String.valueOf(id)));
        commentQueryRequest.setType(Comment.TYPE_COMMENT);
        commentQueryRequest.setSize(NUM_BLOG_COMMENTS);

        // get blog comments
        RestResponse<List<Comment>> restResponse = commentClient.getAllComments(commentQueryRequest);

        return restResponse.getResult();
    }

    // query house comments
    public List<Comment> getHouseComments(long id){

        // house comment query request params
        CommentQueryRequest commentQueryRequest = new CommentQueryRequest();
        commentQueryRequest.setHouseId(id);
        commentQueryRequest.setType(Comment.TYPE_HOUSE);
        commentQueryRequest.setSize(NUM_HOUSE_COMMENTS);

        // get house comments
        RestResponse<List<Comment>> restResponse = commentClient.getAllComments(commentQueryRequest);

        return restResponse.getResult();
    }


    /**
     * add comments
     * add house comments
     * add blog comments
     *
     */
    public void addHouseComment(Long houseId, String content, Long userId){

        // house comment insert request params
        CommentQueryRequest commentQueryRequest = new CommentQueryRequest();

        commentQueryRequest.setHouseId(houseId);
        commentQueryRequest.setContent(content);
        commentQueryRequest.setUserId(userId);
        commentQueryRequest.setType(Comment.TYPE_HOUSE);

        // insert house comment
        RestResponse<Object> restResponse = commentClient.insertComment(commentQueryRequest);
    }

    public void addBlogComment(Integer blogId, String content, Long userId){

        // blog comment insert request params
        CommentQueryRequest commentQueryRequest = new CommentQueryRequest();

        commentQueryRequest.setBlogId(Long.getLong(String.valueOf(blogId)));
        commentQueryRequest.setContent(content);
        commentQueryRequest.setUserId(userId);
        commentQueryRequest.setType(Comment.TYPE_COMMENT);

        // insert blog comment
        RestResponse<Object> restResponse = commentClient.insertComment(commentQueryRequest);
    }

}
