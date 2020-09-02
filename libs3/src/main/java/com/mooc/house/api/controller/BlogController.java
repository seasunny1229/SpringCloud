package com.mooc.house.api.controller;

import com.mooc.house.api.common.CommonConstants;
import com.mooc.house.api.common.PageData;
import com.mooc.house.api.common.PageParams;
import com.mooc.house.api.dao.comment.model.Blog;
import com.mooc.house.api.dao.comment.model.Comment;
import com.mooc.house.api.dao.house.model.House;
import com.mooc.house.api.service.CommentService;
import com.mooc.house.api.service.HouseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * blog
 * blog list
 * blog detail with blog comments
 */

@Controller
public class BlogController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HouseService houseService;

    @RequestMapping(value = "/blog/list", method = {RequestMethod.GET, RequestMethod.POST})
    public String list(Integer pageSize, Integer pageNum, Blog query, ModelMap modelMap){

        // page data blog
        PageData<Blog> ps = commentService.queryBlogs(query, PageParams.build(pageSize, pageNum));

        // hot houses
        List<House> houses = houseService.getHotHouses(CommonConstants.RECOM_SIZE);

        // set model map
        modelMap.put("ps", ps);
        modelMap.put("recomHouses", houses);

        return "/blog/listing";
    }

    @RequestMapping(value = "/blog/detail", method = {RequestMethod.GET, RequestMethod.POST})
    public String blogDetail(int id, ModelMap modelMap){

        // get blog by id
        Blog blog = commentService.queryOneBlog(id);

        // get blog comments
        List<Comment> comments = commentService.getBlogComments(id);

        // get hot houses
        List<House> houses = houseService.getHotHouses(CommonConstants.RECOM_SIZE);

        // set model map
        modelMap.put("blog", blog);
        modelMap.put("commentList", comments);
        modelMap.put("recomHouses", houses);

        return "/blog/detail";
    }

}
