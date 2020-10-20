package com.course.system.service;

import com.alibaba.fastjson.JSON;
import com.course.common.dto.PageDto;
import com.course.common.util.CopyUtil;
import com.course.common.util.UuidUtil;
import com.course.system.domain.Resource;
import com.course.system.domain.ResourceExample;
import com.course.system.dto.ResourceDto;
import com.course.system.mapper.ResourceMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceService {

    @javax.annotation.Resource
    private ResourceMapper resourceMapper;

    /**
     * list query
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        ResourceExample resourceExample = new ResourceExample();
        List<Resource> resourceList = resourceMapper.selectByExample(resourceExample);
        PageInfo<Resource> pageInfo = new PageInfo<>(resourceList);
        pageDto.setTotal(pageInfo.getTotal());
        List<ResourceDto> resourceDtoList = CopyUtil.copyList(resourceList, ResourceDto.class);
        pageDto.setList(resourceDtoList);
    }

    /**
     * save
     */
    public void save(ResourceDto resourceDto) {
        Resource resource = CopyUtil.copy(resourceDto, Resource.class);
        if (StringUtils.isEmpty(resourceDto.getId())) {
            this.insert(resource);
        } else {
            this.update(resource);
        }
    }

    /**
     * insert
     */
    private void insert(Resource resource) {
        //resource.setId(UuidUtil.getShortUuid());
        resourceMapper.insert(resource);
    }

    /**
     * update
     */
    private void update(Resource resource) {
        resourceMapper.updateByPrimaryKey(resource);
    }

    /**
     * delete
     */
    public void delete(String id) {
        resourceMapper.deleteByPrimaryKey(id);
    }


    @Transactional
    public void saveJson(String json){
        List<ResourceDto> jsonList = JSON.parseArray(json, ResourceDto.class);
        List<ResourceDto> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(jsonList)){
            for(ResourceDto d : jsonList){
                d.setParent("");
                add(list, d);
            }
        }
        resourceMapper.deleteByExample(null);
        for(int i=0;i<list.size();i++){
            insert(CopyUtil.copy(list.get(i), Resource.class));
        }
    }


    public void add(List<ResourceDto> list, ResourceDto dto){
        list.add(dto);
        if(!CollectionUtils.isEmpty(dto.getChildren())){
            for(ResourceDto d : dto.getChildren()){
                d.setParent(dto.getId());
                add(list, d);
            }
        }
    }

    public List<ResourceDto> loadTree(){
        ResourceExample resourceExample = new ResourceExample();
        resourceExample.setOrderByClause("id asc");
        List<Resource> resourceList = resourceMapper.selectByExample(resourceExample);
        List<ResourceDto> resourceDtoList = CopyUtil.copyList(resourceList, ResourceDto.class);

        for(int i= resourceDtoList.size() - 1;i >= 0; i--){
            ResourceDto child = resourceDtoList.get(i);
            if(StringUtils.isEmpty(child.getParent())){
                continue;
            }

            for(int j=i-1;j>=0;j--){
                ResourceDto parent = resourceDtoList.get(j);
                if(child.getParent().equals(parent.getId())){
                    if(CollectionUtils.isEmpty(parent.getChildren())){
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(0, child);
                    resourceDtoList.remove(child);
                }
            }

        }

        return resourceDtoList;
    }


}
