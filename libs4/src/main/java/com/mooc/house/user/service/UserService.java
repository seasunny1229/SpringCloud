package com.mooc.house.user.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.mooc.house.user.common.UserException;
import com.mooc.house.user.mapper.UserMapper;
import com.mooc.house.user.model.User;
import com.mooc.house.user.utils.HashUtils;
import com.mooc.house.user.utils.JwtHelper;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MailService mailService;

    /**
     * register
     *
     */
    public int register(User user, String enableUrl){

        // password encryption
        user.setPasswd(HashUtils.encryptPassword(user.getPasswd()));

        // set default user params
        if(user.getName() == null) {
            user.setName("");
        }
        if(user.getPhone() == null){
            user.setPhone("");
        }
        if(user.getAboutme() == null){
            user.setAboutme("");
        }
        if(user.getAvatar() == null){
            user.setAvatar("");
        }
        user.setCreateTime(new Date());
        if(StringUtils.isBlank(enableUrl)){
           user.setEnable(User.ENABLE);
        }

        // insert new user
        int result = userMapper.insert(user);

        // validation check, send an email
        if(!StringUtils.isBlank(enableUrl)){
            registerNotify(user.getEmail(), enableUrl);
        }

        return result;
    }

    public int enable(String key){

        // get email from cache
        String email = redisTemplate.opsForValue().get(key);

        // key existence and validation check
        if(StringUtils.isBlank(email)){
            throw new UserException("invalid key", UserException.Type.USER_NOT_FOUND);
        }

        // update user
        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setEnable(User.ENABLE);
        int result = userMapper.update(updateUser);

        return result;
    }

    private void registerNotify(String email, String enableUrl){

        // redis cache
        String randomKey = HashUtils.hashString(email) + RandomStringUtils.randomAlphabetic(10);
        redisTemplate.opsForValue().set(randomKey, email);
        redisTemplate.expire(randomKey, 1, TimeUnit.HOURS);

        // send validation email
        String content = enableUrl + "?key=" + randomKey;

        // send email
        sendEmail("activate", content, email);
    }

    /**
     * auth/login
     *
     */
    public User auth(String email, String passwd){

        // null check
        if(StringUtils.isBlank(email) || StringUtils.isBlank(passwd)){
            throw new UserException("user auth fail", UserException.Type.USER_AUTH_FAIL);
        }

        // query params
        User user = new User();
        user.setEmail(email);
        user.setPasswd(HashUtils.encryptPassword(passwd));

        // query
        List<User> users = getUserByQuery(user);

        // login and set token
        if(!users.isEmpty()){
            User targetUser = users.get(0);
            doLogin(targetUser);
            return targetUser;
        }

        throw new UserException("user auth fail", UserException.Type.USER_AUTH_FAIL);
    }

    private void doLogin(User user){

        // generate token
        Map<String, String> tokenClaims = ImmutableMap.of(
                "email", user.getEmail(),
                "name", user.getName(),
                "ts", String.valueOf(Instant.now().getEpochSecond())
        );
        String token = JwtHelper.genToken(tokenClaims);

        // renew token
        renewToken(token, user.getEmail());

        // reset token to user
        user.setToken(token);
    }

    private void renewToken(String token, String email){
        redisTemplate.opsForValue().set(email, token);
        redisTemplate.expire(email, 30, TimeUnit.MINUTES);
    }


    /**
     * query user
     *
     */
    public User getLoginUserByToken(String token){

        // get claims from token
        Map<String, String> map = null;
        try {
            map = JwtHelper.verifyToken(token);
        }catch (Exception e){
            throw new UserException("user not login", UserException.Type.USER_NOT_LOGIN);
        }
        if(map == null){
            throw new UserException("user not login", UserException.Type.USER_NOT_LOGIN);
        }

        // get claim info
        String email = map.get("email");
        Long expire = redisTemplate.getExpire(email);
        if(expire == null){
            throw new UserException("user not login", UserException.Type.USER_NOT_LOGIN);
        }

        // renew token
        if(expire > 0){
            renewToken(token, email);
            User user = getUserByEmail(email);
            user.setToken(token);
            return user;
        }

        // not login
        throw new UserException("user not login", UserException.Type.USER_NOT_LOGIN);
    }

    public User getUserById(Long id){

        // get from cache
        String key = "user:" + id;
        String json = redisTemplate.opsForValue().get(key);

        User user;
        if(Strings.isNullOrEmpty(json)){

            // get user from database
            List<User> users = userMapper.selectById(id);

            // set avatar
            setAvatar(users);

            // get target user
            if(!users. isEmpty()){
                user = users.get(0);
            }
            else {
                throw new UserException("user not found", UserException.Type.USER_NOT_FOUND);
            }

            // user object to json
            String userJson = JSON.toJSONString(user);

            // save in cache
            redisTemplate.opsForValue().set(key, userJson);
            redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        }
        else {

            // get from json
            user = JSON.parseObject(json, User.class);

        }

        return user;
    }

    public List<User> getUserByQuery(User user){

        // query
        List<User> users = userMapper.select(user);

        // set avatar image prefix
        setAvatar(users);

        return users;
    }

    private User getUserByEmail(String email){
        User user = new User();
        user.setEmail(email);
        List<User> users = getUserByQuery(user);
        if(!users.isEmpty()){
            return users.get(0);
        }
        throw new UserException("user not found", UserException.Type.USER_NOT_FOUND);
    }


    /**
     * update user info
     *
     */
    public User updateUser(User user){
        if(user.getEmail() == null){
            return null;
        }

        // encrypt password
        if(!Strings.isNullOrEmpty(user.getPasswd())){
            user.setPasswd(HashUtils.encryptPassword(user.getPasswd()));
        }

        // update user info
        userMapper.update(user);

        return getUserByEmail(user.getEmail());
    }

    /**
     * reset password
     *
     */
    public void resetNotify(String email, String url){

        // save email in cache
        String randomKey = "reset_" + RandomStringUtils.randomAlphabetic(10);
        redisTemplate.opsForValue().set(randomKey, url);
        redisTemplate.expire(randomKey, 1, TimeUnit.HOURS);

        // email content
        String content = url + "?key=" + randomKey;

        // send mail
        sendEmail("reset password", content, email);
    }

    public String getResetKeyEmail(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public User resetPassword(String key, String password){

        // get cache key
        String email = getResetKeyEmail(key);

        // query params
        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setPasswd(password);

        // update
        userMapper.update(updateUser);

        return getUserByEmail(email);
    }

    /**
     * invalidate token
     *
     */
    public void invalidateToken(String token){
        Map<String, String> map = JwtHelper.verifyToken(token);
        if(map == null){
            return;
        }
        redisTemplate.delete(map.get("email"));
    }


    /**
     *
     * utils
     */
    private void setAvatar(List<User> users){

    }

    private void sendEmail(String subject, String content, String email){
        mailService.sendSimpleMail(subject, content, email);
    }


}
