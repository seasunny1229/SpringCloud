package com.course.business.mapper;

import com.course.business.dto.CourseDto;
import com.course.business.dto.CoursePageDto;
import com.course.business.dto.SortDto;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyCourseMapper {

    List<CourseDto> list(@Param("pageDto") CoursePageDto pageDto);

    int updateTime(@Param("courseId") String courseId);

    int updateSort(SortDto sortDto);

    int moveSortsBackend(SortDto sortDto);

    int moveSortsForward(SortDto sortDto);

}
