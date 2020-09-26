package com.course.business.service;

import com.course.business.domain.CourseContentFile;
import com.course.business.domain.CourseContentFileExample;
import com.course.business.dto.CourseContentFileDto;
import com.course.business.mapper.CourseContentFileMapper;
import com.course.business.util.CopyUtil;
import com.course.business.util.UuidUtil;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import javax.annotation.Resource;

@Service
public class CourseContentFileService {

    @Resource
    private CourseContentFileMapper courseContentFileMapper;

    /**
     * list query
     */
    public List<CourseContentFileDto> list(String courseId) {
        CourseContentFileExample courseContentFileExample = new CourseContentFileExample();
        CourseContentFileExample.Criteria criteria = courseContentFileExample.createCriteria();
        criteria.andCourseIdEqualTo(courseId);
        List<CourseContentFile> fileList = courseContentFileMapper.selectByExample(courseContentFileExample);
        return CopyUtil.copyList(fileList, CourseContentFileDto.class);
    }


    /**
     * save
     */
    public void save(CourseContentFileDto courseContentFileDto) {
        CourseContentFile courseContentFile = CopyUtil.copy(courseContentFileDto, CourseContentFile.class);
        if (StringUtils.isEmpty(courseContentFileDto.getId())) {
            this.insert(courseContentFile);
        } else {
            this.update(courseContentFile);
        }
    }

    /**
     * insert
     */
    private void insert(CourseContentFile courseContentFile) {
        courseContentFile.setId(UuidUtil.getShortUuid());
        courseContentFileMapper.insert(courseContentFile);
    }

    /**
     * update
     */
    private void update(CourseContentFile courseContentFile) {
        courseContentFileMapper.updateByPrimaryKey(courseContentFile);
    }

    /**
     * delete
     */
    public void delete(String id) {
        courseContentFileMapper.deleteByPrimaryKey(id);
    }
}
