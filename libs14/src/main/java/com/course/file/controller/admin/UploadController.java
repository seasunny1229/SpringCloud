package com.course.file.controller.admin;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetMezzanineInfoResponse;
import com.course.common.dto.ResponseDto;
import com.course.file.dto.FileDto;
import com.course.file.enums.FileUseEnum;
import com.course.file.service.FileService;
import com.course.file.util.Base64ToMultipartFile;
import com.course.file.util.VodUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class UploadController {

    private static final Logger LOG = LoggerFactory.getLogger(UploadController.class);

    public static final String BUSINESS_NAME = "文件上传";

    @Value("${file.domain}")
    private String FILE_DOMAIN;

    @Value("${oss.domain}")
    private String OSS_DOMAIN;

    @Value("${file.path}")
    private String FILE_PATH;

    @Value("${vod.accessKeyId}")
    private String accessKeyId;

    @Value("${vod.accessKeySecret}")
    private String accessKeySecret;

    @Resource
    private FileService fileService;


    @RequestMapping("/upload")
    public ResponseDto upload(@RequestBody FileDto fileDto) throws Exception{

        // base info
        String use = fileDto.getUse();
        String key = fileDto.getKey();
        String suffix = fileDto.getSuffix();

        // multipart file
        String shardBase64 = fileDto.getShard();
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);

        // file dir (/course/...)
        FileUseEnum useEnum = FileUseEnum.getByCode(use);
        String dir = useEnum.name().toLowerCase();

        // file full dir
        File fullDir = new File(FILE_PATH + dir);
        if(!fullDir.exists()){
            fullDir.mkdir();
        }

        // file path
        // course/6sfSqfOwzmik4A4icMYuUe.mp4
        String path = new StringBuffer(dir)
                .append(File.separator)
                .append(key)
                .append(".")
                .append(suffix)
                .toString();

        // local path
        // course/6sfSqfOwzmik4A4icMYuUe.mp4.1
        String localPath = new StringBuffer(path)
                .append(".")
                .append(fileDto.getShardIndex())
                .toString();

        // full local path
        String fullPath = FILE_PATH + localPath;
        File dest = new File(fullPath);

        // transfer file to dest
        shard.transferTo(dest);

        // save file meta info
        fileDto.setPath(path);
        fileService.save(fileDto);

        // response dto
        ResponseDto responseDto = new ResponseDto();
        fileDto.setPath(FILE_DOMAIN + path);
        responseDto.setContent(fileDto);

        // merge files
        if (fileDto.getShardIndex().equals(fileDto.getShardTotal())) {
            this.merge(fileDto);
        }

        return responseDto;
    }

    public void merge(FileDto fileDto) throws Exception{

        //http://127.0.0.1:9000/file/f/course/6sfSqfOwzmik4A4icMYuUe.mp4
        String path = fileDto.getPath();

        //course/6sfSqfOwzmik4A4icMYuUe.mp4
        path = path.replace(FILE_DOMAIN, "");

        // total parts
        Integer shardTotal = fileDto.getShardTotal();

        //  local file full path
        File newFile = new File(FILE_PATH + path);

        FileOutputStream fileOutputStream = new FileOutputStream(newFile, true);

        // part file
        FileInputStream fileInputStream = null;

        // init byte array and length
        byte[] bytes = new byte[10 * 1024 * 1024];
        int length;

        // merge file
        try {

            for(int i=0;i<shardTotal;i++){

                //  course\6sfSqfOwzmik4A4icMYuUe.mp4.1
                File file = new File(FILE_PATH + path + "." + (1+i));

                // read file from local
                fileInputStream = new FileInputStream(file);

                // read and write
                while ((length = fileInputStream.read(bytes))!= -1){
                    fileOutputStream.write(bytes, 0 , length);
                }
            }


        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                if(fileInputStream != null){
                    fileInputStream.close();
                }
                fileOutputStream.close();
            }catch (Exception e){

            }
        }

        System.gc();
        Thread.sleep(100);


        // delete raw multipart files
        for (int i = 0; i < shardTotal; i++) {
            String filePath = FILE_PATH + path + "." + (i + 1);
            File file = new File(filePath);
            boolean result = file.delete();
        }


    }

    @GetMapping("/check/{key}")
    public ResponseDto check(@PathVariable("key") String key) throws Exception{

        ResponseDto responseDto = new ResponseDto();

        // find file meta info
        FileDto fileDto = fileService.findByKey(key);
        if(fileDto != null){

            if(StringUtils.isEmpty(fileDto.getVod())){
                fileDto.setPath(OSS_DOMAIN + fileDto.getPath());
            }
            else {
                DefaultAcsClient defaultAcsClient = VodUtil.initVodClient(accessKeyId, accessKeySecret);
                GetMezzanineInfoResponse response = VodUtil.getMezzanineInfo(defaultAcsClient, fileDto.getVod());
                String fileUrl = response.getMezzanine().getFileURL();
                fileDto.setPath(fileUrl);
            }

        }

        responseDto.setContent(fileDto);

        return responseDto;
    }


}
