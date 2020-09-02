package com.mooc.house.api.controller;


import com.mooc.house.api.common.UserContext;
import com.mooc.house.api.dao.user.model.User;
import com.mooc.house.api.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * comment
 * add house comment
 * add blog comment
 */

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;


    @RequestMapping(value = "/comment/leaveComment")
    public String leaveComment(String content, Long houseId, ModelMap modelMap){

        // get user
        User user = UserContext.getUser();
        Long userId = user.getId();

        // insert house comment
        commentService.addHouseComment(houseId, content, userId);

        return "redirect:/house/detail?id=" + houseId;
    }

    @RequestMapping(value = "/comment/leaveBlogComment")
    public String leaveBlogComments(String content, Integer blogId, ModelMap modelMap){

        // get user
        User user = UserContext.getUser();
        Long userId = user.getId();

        // insert blog comment
        commentService.addBlogComment(blogId, content, userId);

        return "redirect:/blog/detail?id=" + blogId;
    }

}
