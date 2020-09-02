package com.mooc.house.api.service;


import com.mooc.house.api.dao.common.RestResponse;
import com.mooc.house.api.dao.user.TestUserClient;
import com.mooc.house.api.dao.user.model.User;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * query user
 * register/activate
 * login/logout
 * update user info
 *
 */

@Service
public class UserService {


    @Autowired
    private TestUserClient userClient;

    private String domainName;


    /**
     *
     * query
     *
     */
    public User getUserById(Long id){

        // rest response
        RestResponse<User> user = userClient.getUserById(id);

        // user
        return user.getResult();
    }

    public List<User> getUserByQuery(User query){

        // rest response
        RestResponse<List<User>> restResponse = userClient.getUsers(query);

        List<User> users = restResponse.getResult();

        return users;
    }

    public boolean isExist(String email){
        return getUser(email) != null;
    }

    private User getUser(String email) {
        User queryUser = new User();
        queryUser.setEmail(email);
        List<User> users =  getUserByQuery(queryUser);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }


    /**
     *
     * update
     *
     */
    public User updateUser(User user){

        // update user info
        RestResponse<User> restResponse = userClient.update(user);

        return restResponse.getResult();
    }


    /**
     *
     * login/logout
     *
     */
    public User auth(String userEmail, String password){
        if(StringUtils.isBlank(userEmail) || StringUtils.isBlank(password)){
            return null;
        }

        // query params
        User user = new User();
        user.setEmail(userEmail);
        user.setPasswd(password);

        // auth
        User result;
        try{
            RestResponse<User> restResponse = userClient.login(user);
            result = restResponse.getResult();
        }catch (Exception e){
            return null;
        }

        return result;
    }

    public void logout(String token){

        // logout
        RestResponse<Object> restResponse = userClient.logout(token);
    }


    /**
     *
     * register
     * activate
     *
     */
    public boolean addAccount(User account){

        // upload avatar images

        // enable url
        //account.setEnableUrl("http://" + domainName + "/accounts/verify");

        RestResponse<Object> restResponse =  userClient.register(account);

        return true;
    }

    public boolean enable(String key){

        // activate account
        userClient.enable(key);

        return true;
    }




}
