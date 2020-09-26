package com.course.file.service;

import com.course.common.dto.PageDto;
import com.course.common.util.CopyUtil;
import com.course.common.util.UuidUtil;
import com.course.file.domain.File;
import com.course.file.domain.FileExample;
import com.course.file.dto.FileDto;
import com.course.file.mapper.FileMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

@Service
public class FileService {

    @Resource
    private FileMapper fileMapper;

    /**
     * list query
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        FileExample fileExample = new FileExample();
        List<File> fileList = fileMapper.selectByExample(fileExample);
        PageInfo<File> pageInfo = new PageInfo<>(fileList);
        pageDto.setTotal(pageInfo.getTotal());
        List<FileDto> fileDtoList = CopyUtil.copyList(fileList, FileDto.class);
        pageDto.setList(fileDtoList);
    }

    /**
     * save
     */
    public void save(FileDto fileDto) {
        File file = CopyUtil.copy(fileDto, File.class);
        File fileDb = selectByKey(fileDto.getKey());
        if (fileDb == null) {
            this.insert(file);
        } else {
            fileDb.setShardIndex(fileDto.getShardIndex());
            this.update(fileDb);
        }
    }

    /**
     * insert
     */
    private void insert(File file) {
        Date now = new Date();
        file.setCreatedAt(now);
        file.setUpdatedAt(now);
        file.setId(UuidUtil.getShortUuid());
        fileMapper.insert(file);
    }

    /**
     * update
     */
    private void update(File file) {
        file.setUpdatedAt(new Date());
        fileMapper.updateByPrimaryKey(file);
    }

    /**
     * delete
     */
    public void delete(String id) {
        fileMapper.deleteByPrimaryKey(id);
    }

    public File selectByKey(String key){
        FileExample fileExample = new FileExample();
        fileExample.createCriteria().andKeyEqualTo(key);
        List<File> fileList = fileMapper.selectByExample(fileExample);
        if(CollectionUtils.isEmpty(fileList)){
            return null;
        }
        else {
            return fileList.get(0);
        }
    }

    public FileDto findByKey(String key){
        return CopyUtil.copy(selectByKey(key), FileDto.class);
    }

}
