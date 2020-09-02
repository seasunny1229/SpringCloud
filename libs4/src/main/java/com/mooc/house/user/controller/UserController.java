package com.mooc.house.user.controller;

import com.mooc.house.user.common.RestResponse;
import com.mooc.house.user.model.User;
import com.mooc.house.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * register
     * @param user must set email, password and type
     * can send an validation email to activate user or just directly enable the user
     *
     */
    @PostMapping("/register")
    public RestResponse<Object> register(@RequestBody User user){
        int result = userService.register(user, user.getEnableUrl());
        return RestResponse.success(result);
    }

    @PostMapping("/enable")
    public RestResponse<Object> enable(@RequestBody String key){
        int result = userService.enable(key);
        return RestResponse.success(result);
    }

    /**
     * login/logout
     *
     */
    @PostMapping("/login")
    public RestResponse<User> login(@RequestBody User user){
        User result = userService.auth(user.getEmail(), user.getPasswd());
        return RestResponse.success(result);
    }

    @PostMapping("/logout")
    public RestResponse<Object> logout(@RequestBody String token){

        // invalidate token
        userService.invalidateToken(token);

        return RestResponse.success();
    }

    /**
     * update
     *
     */
    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<User> update(@RequestBody User user){

        // update user info
        User result = userService.updateUser(user);

        return RestResponse.success(result);
    }

    /**
     * reset password
     *
     */
    @GetMapping("/reset-notify")
    public RestResponse<Object> resetPasswordNotify(@RequestParam("email") String email, @RequestParam("url") String url){

        // save email info in cache and send confirming email
        userService.resetNotify(email, url);

        return RestResponse.success();
    }

    @GetMapping("/email-key")
    public RestResponse<String> getKeyEmail(@RequestParam("key") String key){

        // get email from cache
        String email = userService.getResetKeyEmail(key);

        return RestResponse.success(email);
    }

    @PostMapping("/reset-password")
    public RestResponse<User> resetPassword(@RequestBody User user){

        String email = user.getEmail();
        String password = user.getPasswd();

        User target = userService.resetPassword(email, password);

        return RestResponse.success(target);
    }


    /**
     * query
     *
     */
    @GetMapping("/{id}")
    public RestResponse<User> getUserById(Long id){
        User user = userService.getUserById(id);
        return RestResponse.success(user);
    }

    @PostMapping("/list")
    public RestResponse<List<User>> getUsers(@RequestBody User user){
        List<User> users = userService.getUserByQuery(user);
        return RestResponse.success(users);
    }

    @GetMapping
    public RestResponse<User> getUserByToken(@RequestParam("token") String token){
        User user = userService.getLoginUserByToken(token);
        return RestResponse.success(user);
    }

}
