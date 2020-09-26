package com.course.business.controller.admin;

import com.course.business.dto.ChapterDto;
import com.course.business.dto.ChapterPageDto;
import com.course.business.dto.PageDto;
import com.course.business.dto.ResponseDto;
import com.course.business.service.ChapterService;
import com.course.business.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/chapter")
public class ChapterController {

    private static final Logger LOG = LoggerFactory.getLogger(ChapterController.class);
    public static final String BUSINESS_NAME = "大章";

    @Resource
    private ChapterService chapterService;

    /**
     * test
     */
    @GetMapping("/test")
    public ResponseDto getById(){
        ResponseDto responseDto = new ResponseDto();
        PageDto pageDto = new PageDto();
        pageDto.setPage(1);
        pageDto.setSize(3);
        chapterService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     *
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody ChapterPageDto chapterPageDto) {

        // create response dto
        ResponseDto responseDto = new ResponseDto();

        // validation check
        ValidatorUtil.require(chapterPageDto.getCourseId(), "课程ID");

        // get chapter page
        chapterService.pageListByCourse(chapterPageDto);

        // response
        responseDto.setContent(chapterPageDto);

        return responseDto;
    }

    /**
     *
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody ChapterDto chapterDto) {
        // 保存校验
        ValidatorUtil.require(chapterDto.getName(), "名称");
        ValidatorUtil.require(chapterDto.getCourseId(), "课程ID");
        ValidatorUtil.length(chapterDto.getCourseId(), "课程ID", 1, 8);

        ResponseDto responseDto = new ResponseDto();
        chapterService.save(chapterDto);
        responseDto.setContent(chapterDto);
        return responseDto;
    }


    /**
     *
     */
    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        chapterService.delete(id);
        return responseDto;
    }
}
