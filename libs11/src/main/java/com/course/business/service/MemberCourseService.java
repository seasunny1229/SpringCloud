package com.course.business.service;

import com.course.business.domain.MemberCourse;
import com.course.business.domain.MemberCourseExample;
import com.course.business.dto.MemberCourseDto;
import com.course.business.dto.PageDto;
import com.course.business.mapper.MemberCourseMapper;
import com.course.business.util.CopyUtil;
import com.course.business.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

@Service
public class MemberCourseService {

    @Resource
    private MemberCourseMapper memberCourseMapper;

    /**
     * list query
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        MemberCourseExample memberCourseExample = new MemberCourseExample();
        List<MemberCourse> memberCourseList = memberCourseMapper.selectByExample(memberCourseExample);
        PageInfo<MemberCourse> pageInfo = new PageInfo<>(memberCourseList);
        pageDto.setTotal(pageInfo.getTotal());
        List<MemberCourseDto> memberCourseDtoList = CopyUtil.copyList(memberCourseList, MemberCourseDto.class);
        pageDto.setList(memberCourseDtoList);
    }

    /**
     * save
     */
    public void save(MemberCourseDto memberCourseDto) {
        MemberCourse memberCourse = CopyUtil.copy(memberCourseDto, MemberCourse.class);
        if (StringUtils.isEmpty(memberCourseDto.getId())) {
            this.insert(memberCourse);
        } else {
            this.update(memberCourse);
        }
    }

    /**
     * insert
     */
    private void insert(MemberCourse memberCourse) {
        Date now = new Date();
        memberCourse.setId(UuidUtil.getShortUuid());
        memberCourseMapper.insert(memberCourse);
    }

    /**
     * update
     */
    private void update(MemberCourse memberCourse) {
        memberCourseMapper.updateByPrimaryKey(memberCourse);
    }

    /**
     * delete
     */
    public void delete(String id) {
        memberCourseMapper.deleteByPrimaryKey(id);
    }


    public MemberCourseDto enroll(MemberCourseDto memberCourseDto){
        MemberCourse memberCourseDb = this.select(memberCourseDto.getMemberId(), memberCourseDto.getCourseId());
        if(memberCourseDb == null){
            MemberCourse memberCourse = CopyUtil.copy(memberCourseDto, MemberCourse.class);
            this.insert(memberCourse);
            return CopyUtil.copy(memberCourse, MemberCourseDto.class);
        }
        else {
            return CopyUtil.copy(memberCourseDb, MemberCourseDto.class);
        }
    }


    public MemberCourse select(String memberId, String courseId){
        MemberCourseExample memberCourseExample = new MemberCourseExample();
        memberCourseExample.createCriteria()
                .andCourseIdEqualTo(courseId)
                .andMemberIdEqualTo(memberId);
        List<MemberCourse> memberCourseList = memberCourseMapper.selectByExample(memberCourseExample);
        if (CollectionUtils.isEmpty(memberCourseList)) {
            return null;
        }
        else {
            return memberCourseList.get(0);
        }
    }

    public MemberCourseDto getEnroll(MemberCourseDto memberCourseDto){
        MemberCourse memberCourse = this.select(memberCourseDto.getMemberId(), memberCourseDto.getCourseId());
        return CopyUtil.copy(memberCourse, MemberCourseDto.class);
    }


}
