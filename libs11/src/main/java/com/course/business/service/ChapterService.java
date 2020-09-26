package com.course.business.service;

import com.course.business.domain.Chapter;
import com.course.business.domain.ChapterExample;
import com.course.business.dto.ChapterDto;
import com.course.business.dto.ChapterPageDto;
import com.course.business.dto.PageDto;
import com.course.business.mapper.ChapterMapper;
import com.course.business.util.CopyUtil;
import com.course.business.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import javax.annotation.Resource;

@Service
public class ChapterService {

    @Resource
    private ChapterMapper chapterMapper;

    /**
     *
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        ChapterExample chapterExample = new ChapterExample();
        List<Chapter> chapterList = chapterMapper.selectByExample(chapterExample);
        PageInfo<Chapter> pageInfo = new PageInfo<>(chapterList);
        pageDto.setTotal(pageInfo.getTotal());
        List<ChapterDto> chapterDtoList = CopyUtil.copyList(chapterList, ChapterDto.class);
        pageDto.setList(chapterDtoList);
    }

    public List<ChapterDto> listByCourse(String courseId){

        // chapter example
        ChapterExample chapterExample = new ChapterExample();

        // criteria
        chapterExample.createCriteria().andCourseIdEqualTo(courseId);

        // get chapters
        List<Chapter> chapterList = chapterMapper.selectByExample(chapterExample);

        // get chapter dtos
        List<ChapterDto> chapterDtoList = CopyUtil.copyList(chapterList, ChapterDto.class);

        return chapterDtoList;
    }

    public void pageListByCourse(ChapterPageDto chapterPageDto){

        // page
        PageHelper.startPage(chapterPageDto.getPage(), chapterPageDto.getSize());

        // chapter example
        ChapterExample chapterExample = new ChapterExample();

        // criteria
        ChapterExample.Criteria criteria = chapterExample.createCriteria();

        // add criterion
        if(!StringUtils.isEmpty(chapterPageDto.getCourseId())){
            criteria.andCourseIdEqualTo(chapterPageDto.getCourseId());
        }

        // get chapters
        List<Chapter> chapterList = chapterMapper.selectByExample(chapterExample);

        // page info
        PageInfo<Chapter> pageInfo = new PageInfo<>(chapterList);

        // chapter page dto: total
        chapterPageDto.setTotal(pageInfo.getTotal());

        // chapter dto
        List<ChapterDto> chapterDtoList = CopyUtil.copyList(chapterList, ChapterDto.class);

        // chapter page dto: data
        chapterPageDto.setList(chapterDtoList);
    }


    /**
     *
     */
    public void save(ChapterDto chapterDto) {
        Chapter chapter = CopyUtil.copy(chapterDto, Chapter.class);
        if (StringUtils.isEmpty(chapterDto.getId())) {
            this.insert(chapter);
        } else {
            this.update(chapter);
        }
    }

    /**
     *
     */
    private void insert(Chapter chapter) {
        chapter.setId(UuidUtil.getShortUuid());
        chapterMapper.insert(chapter);
    }

    /**
     *
     */
    private void update(Chapter chapter) {
        chapterMapper.updateByPrimaryKey(chapter);
    }

    /**
     *
     */
    public void delete(String id) {
        chapterMapper.deleteByPrimaryKey(id);
    }
}
