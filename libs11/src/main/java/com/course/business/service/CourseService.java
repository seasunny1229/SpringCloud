package com.course.business.service;

import com.course.business.domain.Course;
import com.course.business.domain.CourseContent;
import com.course.business.domain.CourseExample;
import com.course.business.dto.ChapterDto;
import com.course.business.dto.CourseContentDto;
import com.course.business.dto.CourseDto;
import com.course.business.dto.CoursePageDto;
import com.course.business.dto.PageDto;
import com.course.business.dto.SectionDto;
import com.course.business.dto.SortDto;
import com.course.business.dto.TeacherDto;
import com.course.business.enums.CourseStatusEnum;
import com.course.business.mapper.CourseContentMapper;
import com.course.business.mapper.CourseMapper;
import com.course.business.mapper.MyCourseMapper;
import com.course.business.util.CopyUtil;
import com.course.business.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

@Service
public class CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Autowired
    private MyCourseMapper myCourseMapper;

    @Autowired
    private CourseCategoryService courseCategoryService;

    @Autowired
    private CourseContentMapper courseContentMapper;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private SectionService sectionService;



    /**
     * list query
     */
    public void list(CoursePageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());

        // get all courses
        List<CourseDto> courseDtoList = myCourseMapper.list(pageDto);

        // page info
        PageInfo<CourseDto> pageInfo = new PageInfo<>(courseDtoList);

        pageDto.setTotal(pageInfo.getTotal());
        pageDto.setList(courseDtoList);
    }

    public List<CourseDto> listNew(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        CourseExample courseExample = new CourseExample();
        courseExample.createCriteria().andStatusEqualTo(CourseStatusEnum.PUBLISH.getCode());
        courseExample.setOrderByClause("created_at desc");
        List<Course> courseList = courseMapper.selectByExample(courseExample);
        return CopyUtil.copyList(courseList, CourseDto.class);
    }

    // region update
    /**
     * save
     */
    @Transactional
    public void save(CourseDto courseDto) {
        Course course = CopyUtil.copy(courseDto, Course.class);
        if (StringUtils.isEmpty(courseDto.getId())) {
            this.insert(course);
        } else {
            this.update(course);
        }
        courseCategoryService.saveBatch(course.getId(), courseDto.getCategorys());
    }

    /**
     * insert
     */
    private void insert(Course course) {
        Date now = new Date();
        course.setCreatedAt(now);
        course.setUpdatedAt(now);
        course.setId(UuidUtil.getShortUuid());
        courseMapper.insert(course);
    }

    /**
     * update
     */
    private void update(Course course) {
        course.setUpdatedAt(new Date());
        courseMapper.updateByPrimaryKey(course);
    }

    /**
     * delete
     */
    public void delete(String id) {
        courseMapper.deleteByPrimaryKey(id);
    }
    // endregion

    public void updateTime(String courseId){
        myCourseMapper.updateTime(courseId);
    }

    public CourseContentDto findContent(String id){
        CourseContent courseContent = courseContentMapper.selectByPrimaryKey(id);
        if(courseContent == null){
            return null;
        }
        return CopyUtil.copy(courseContent, CourseContentDto.class);
    }

    public int saveContent(CourseContentDto contentDto){
        CourseContent courseContent = CopyUtil.copy(contentDto, CourseContent.class);
        int i= courseContentMapper.updateByPrimaryKeyWithBLOBs(courseContent);
        if(i == 0){
            i = courseContentMapper.insert(courseContent);
        }
        return i;
    }

    @Transactional
    public void sort(SortDto sortDto){
        myCourseMapper.updateSort(sortDto);
        if(sortDto.getNewSort() > sortDto.getOldSort()){
            myCourseMapper.moveSortsForward(sortDto);
        }

        if(sortDto.getNewSort() < sortDto.getOldSort()){
            myCourseMapper.moveSortsBackend(sortDto);
        }
    }

    public CourseDto findCourse(String id){

        // find course
        Course course = courseMapper.selectByPrimaryKey(id);
        if(course == null || !CourseStatusEnum.PUBLISH.getCode().equals(course.getStatus())){
            return null;
        }

        // course dto
        CourseDto courseDto = CopyUtil.copy(course, CourseDto.class);

        // find course content
        CourseContent content = courseContentMapper.selectByPrimaryKey(id);
        if(content != null){
            courseDto.setContent(content.getContent());
        }

        // find teacher
        TeacherDto teacherDto = teacherService.findById(courseDto.getTeacherId());
        courseDto.setTeacher(teacherDto);

        // chapter
        List<ChapterDto> chapterDtoList = chapterService.listByCourse(id);
        courseDto.setChapters(chapterDtoList);

        // section
        List<SectionDto> sectionDtoList = sectionService.listByCourse(id);
        courseDto.setSections(sectionDtoList);

        return courseDto;
    }


}
