package com.course.system.service;

import com.course.common.dto.PageDto;
import com.course.common.util.CopyUtil;
import com.course.common.util.UuidUtil;
import com.course.system.domain.Role;
import com.course.system.domain.RoleExample;
import com.course.system.domain.RoleResource;
import com.course.system.domain.RoleResourceExample;
import com.course.system.domain.RoleUser;
import com.course.system.domain.RoleUserExample;
import com.course.system.dto.RoleDto;
import com.course.system.mapper.RoleMapper;
import com.course.system.mapper.RoleResourceMapper;
import com.course.system.mapper.RoleUserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Autowired
    private RoleUserMapper roleUserMapper;

    /**
     * list query
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        RoleExample roleExample = new RoleExample();
        List<Role> roleList = roleMapper.selectByExample(roleExample);
        PageInfo<Role> pageInfo = new PageInfo<>(roleList);
        pageDto.setTotal(pageInfo.getTotal());
        List<RoleDto> roleDtoList = CopyUtil.copyList(roleList, RoleDto.class);
        pageDto.setList(roleDtoList);
    }

    /**
     * save
     */
    public void save(RoleDto roleDto) {
        Role role = CopyUtil.copy(roleDto, Role.class);
        if (StringUtils.isEmpty(roleDto.getId())) {
            this.insert(role);
        } else {
            this.update(role);
        }
    }

    /**
     * insert
     */
    private void insert(Role role) {
        role.setId(UuidUtil.getShortUuid());
        roleMapper.insert(role);
    }

    /**
     * update
     */
    private void update(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    /**
     * delete
     */
    public void delete(String id) {
        roleMapper.deleteByPrimaryKey(id);
    }

    public void saveResource(RoleDto roleDto){
        String roleId = roleDto.getId();
        List<String> resourceIds = roleDto.getResourceIds();

        RoleResourceExample example = new RoleResourceExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        roleResourceMapper.deleteByExample(example);

        for(int i=0;i<resourceIds.size();i++){
            RoleResource roleResource = new RoleResource();
            roleResource.setId(UuidUtil.getShortUuid());
            roleResource.setRoleId(roleId);
            roleResource.setResourceId(resourceIds.get(i));
            roleResourceMapper.insert(roleResource);
        }
    }

    public List<String> listResource(String roleId){
        RoleResourceExample example = new RoleResourceExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<RoleResource> roleResourceList = roleResourceMapper.selectByExample(example);
        List<String> resourceIdList = new ArrayList<>();
        for(int i=0,l=roleResourceList.size();i<l;i++){
            resourceIdList.add(roleResourceList.get(i).getResourceId());
        }
        return resourceIdList;
    }

    public void saveUser(RoleDto roleDto){
        String roleId = roleDto.getId();
        List<String> userIdList = roleDto.getUserIds();

        RoleUserExample example = new RoleUserExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        roleUserMapper.deleteByExample(example);

        for(int i=0;i<userIdList.size();i++){
            RoleUser roleUser = new RoleUser();
            roleUser.setId(UuidUtil.getShortUuid());
            roleUser.setRoleId(roleId);
            roleUser.setUserId(userIdList.get(i));
            roleUserMapper.insert(roleUser);
        }

    }

    public List<String> listUser(String roleId){
        RoleUserExample example = new RoleUserExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<RoleUser> roleUserList = roleUserMapper.selectByExample(example);
        List<String> userIdList = new ArrayList<>();
        for(int i=0,l=roleUserList.size();i<l;i++){
            userIdList.add(roleUserList.get(i).getUserId());
        }
        return userIdList;
    }

}

