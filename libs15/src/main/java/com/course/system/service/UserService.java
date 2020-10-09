package com.course.system.service;

import com.alibaba.fastjson.JSON;
import com.course.common.dto.PageDto;
import com.course.common.util.CopyUtil;
import com.course.common.util.UuidUtil;
import com.course.system.domain.User;
import com.course.system.domain.UserExample;
import com.course.system.dto.LoginUserDto;
import com.course.system.dto.ResourceDto;
import com.course.system.dto.UserDto;
import com.course.system.exception.BusinessException;
import com.course.system.exception.BusinessExceptionCode;
import com.course.system.mapper.MyUserMapper;
import com.course.system.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private MyUserMapper myUserMapper;

    /**
     * list query
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        UserExample userExample = new UserExample();
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        pageDto.setTotal(pageInfo.getTotal());
        List<UserDto> userDtoList = CopyUtil.copyList(userList, UserDto.class);
        pageDto.setList(userDtoList);
    }

    /**
     * save
     */
    public void save(UserDto userDto) {
        User user = CopyUtil.copy(userDto, User.class);
        if (StringUtils.isEmpty(userDto.getId())) {
            this.insert(user);
        } else {
            this.update(user);
        }
    }

    /**
     * insert
     */
    private void insert(User user) {
        user.setId(UuidUtil.getShortUuid());

        // try to get existed user from database by username
        User userDb = this.selectByLoginName(user.getLoginName());

        // if the username has been already existed, fail
        if(userDb != null){
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
        }

        // insert new user
        userMapper.insert(user);
    }

    /**
     * update
     */
    private void update(User user) {


        // remove password
        // do not update password
        user.setPassword(null);

        // update user info
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * delete
     */
    public void delete(String id) {
        userMapper.deleteByPrimaryKey(id);
    }


    public User selectByLoginName(String loginName){

        // find user by user login name
        UserExample userExample = new UserExample();
        userExample.createCriteria().andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(userExample);

        // return user
        if(userList.isEmpty()){
            return null;
        }
        else{
            return userList.get(0);
        }
    }

    public void savePassword(UserDto userDto){

        // update user's password, selected by user primary key id
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        userMapper.updateByPrimaryKeySelective(user);
    }

    public LoginUserDto login(UserDto userDto){

        // get user
        User user = selectByLoginName(userDto.getLoginName());

        if(user == null){
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }
        else {
            if(user.getPassword().equals(userDto.getPassword())){

                // get login user dto
                LoginUserDto loginUserDto = CopyUtil.copy(user, LoginUserDto.class);

                // auth
                setAuth(loginUserDto);
                return loginUserDto;
            }
            else {
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }

    }


    private void setAuth(LoginUserDto loginUserDto){

        // get user's resources
        List<ResourceDto> resourceDtoList = myUserMapper.findResources(loginUserDto.getId());
        loginUserDto.setResources(resourceDtoList);

        // set requests
        HashSet<String> requestSet = new HashSet<>();
        if(!CollectionUtils.isEmpty(resourceDtoList)){
            for(int i=0,l=resourceDtoList.size();i<l;i++){
                ResourceDto resourceDto = resourceDtoList.get(i);
                String arrayString =resourceDto.getRequest();
                List<String> requestList = JSON.parseArray(arrayString, String.class);
                if(!CollectionUtils.isEmpty(requestList)){
                    requestSet.addAll(requestList);
                }
            }
        }
        loginUserDto.setRequests(requestSet);
    }

}
