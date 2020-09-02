package com.mooc.house.api.controller;


import com.mooc.house.api.common.ResultMsg;
import com.mooc.house.api.common.UserContext;
import com.mooc.house.api.dao.user.model.Agency;
import com.mooc.house.api.dao.user.model.User;
import com.mooc.house.api.service.AgencyService;
import com.mooc.house.api.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * register
 * login/logout
 * update user info
 * reset password
 */


@Controller
public class UserController {


    @Autowired
    private UserService userService;


    @Autowired
    private AgencyService agencyService;


    /**
     *
     * register
     *
     */

    @RequestMapping(value = "accounts/register", method={RequestMethod.POST,RequestMethod.GET})
    public String accountsSubmit(User account, ModelMap modelMap){

        // null check
        if(account == null || account.getName() == null){


            // get all agencies
            List<Agency> agencies =  agencyService.getAllAgencies();

            // model map
            modelMap.put("agencylist", agencies);

            return "/user/accounts/register";
        }

        // account info validation check
        ResultMsg resultMsg = UserHelper.validate(account);

        // register
        if(resultMsg.isSuccess()){
            boolean exist = userService.isExist(account.getEmail());
            if(!exist){

                // register
                userService.addAccount(account);

                // model map
                modelMap.put("success_email", account.getEmail());

                return "/user/accounts/registerSubmit";
            }
            else {
                return "redirect:/accounts/register?" + ResultMsg.errorMsg("wrong email address") .asUrlParams();
            }
        }
        else {
          return "redirect:/accounts/register?" + ResultMsg.errorMsg("wrong params") .asUrlParams();
        }
    }


    @RequestMapping("/accounts/verify")
    public String verify(String key){
        boolean result = userService.enable(key);
        if(result){
            return "redirect:/index?" + ResultMsg.successMsg("success").asUrlParams();
        }
        else {
            return "redirect:/accounts/signin?" + ResultMsg.errorMsg("fail to activate account").asUrlParams();
        }
    }


    /**
     *
     * login/logout
     *
     */

    @RequestMapping(value = "/accounts/signin", method={RequestMethod.POST,RequestMethod.GET})
    public String loginSubmit(HttpServletRequest request){

        //basic info
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // null check
        if(username == null || password == null){
            request.setAttribute("target", request.getParameter("target"));
            return "/user/accounts/signin";
        }

        // login
        User user = userService.auth(username, password);

        if(user == null){
            return "redirect:/accounts/signin?" + "username=" + username + "&"  + ResultMsg.errorMsg("usrname or password is wrong").asUrlParams();
        }
        else {

            // set user in user context
            UserContext.setUser(user);

            return StringUtils.isNotBlank(request.getParameter("target"))? "redirect:" + request.getParameter("target") : "redirect:/index";
        }

    }


    @RequestMapping("/accounts/logout")
    public String logout(HttpServletRequest request){

        // user
        User user = UserContext.getUser();

        //logout
        userService.logout(user.getToken());

        return "/redirect:/index";
    }


    /**
     *
     * update user info
     */
    @RequestMapping(value = "/accounts/profile", method={RequestMethod.POST,RequestMethod.GET})
    public String profile(ModelMap modelMap){

        List<Agency> list = agencyService.getAllAgencies();

        modelMap.addAttribute("agencyList", list);

        return "/user/accounts/profile";
    }

    @RequestMapping(value = "accounts/profileSubmit", method={RequestMethod.POST,RequestMethod.GET})
    public String profileSubmit(HttpServletRequest request, User updateUser, ModelMap modelMap){

        if(updateUser.getEmail() == null){
            return "redirect:/accounts/profile?" + ResultMsg.errorMsg("email cannot be null").asUrlParams();
        }

        User user = userService.updateUser(updateUser);

        UserContext.setUser(user);

        return "redirect:/accounts/profile?" + ResultMsg.successMsg("update success").asUrlParams();

    }

}
