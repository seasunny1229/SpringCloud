package com.course.file.controller.admin;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.AppendObjectRequest;
import com.aliyun.oss.model.AppendObjectResult;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.course.common.dto.ResponseDto;
import com.course.common.util.UuidUtil;
import com.course.file.dto.FileDto;
import com.course.file.enums.FileUseEnum;
import com.course.file.service.FileService;
import com.course.file.util.Base64ToMultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/admin")
public class OssController {
    public static final String BUSINESS_NAME = "";

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.bucket}")
    private String bucket;

    @Value("${oss.domain}")
    private String ossDomain;

    @Autowired
    private FileService fileService;

    @PostMapping("oss-append")
    public ResponseDto fileUpload(@RequestBody FileDto fileDto) throws Exception{

        // file base info
        // file use (used for course or other)
        String use = fileDto.getUse();

        // random generated key
        String key = fileDto.getKey();

        // .mp4 or other suffix
        String suffix = fileDto.getSuffix();

        // file meta info
        Integer sharedIndex = fileDto.getShardIndex();
        Integer sharedSize = fileDto.getShardSize();

        // multipart file
        String sharedBase64 = fileDto.getShard();
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(sharedBase64);

        // file primary dir obtained from file use info
        FileUseEnum fileUseEnum = FileUseEnum.getByCode(use);
        String dir = fileUseEnum.name().toLowerCase();

        // file path example: course/6sfSqfOwzmik4A4icMYuUe.mp4
        String path = new StringBuffer(dir)
                .append("/")
                .append(key)
                .append(".")
                .append(suffix)
                .toString();

        // oss
        OSS oSSClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // meta
        ObjectMetadata metadata = new ObjectMetadata();

        // type
        metadata.setContentType("text/plain");

        // request
        // get file byte stream from multi part file
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(shard.getBytes());

        // append request
        AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucket, path, byteArrayInputStream, metadata);

        // append position
        appendObjectRequest.setPosition((long) ((sharedIndex - 1) * sharedSize));

        // do request and get result
        AppendObjectResult appendObjectResult = oSSClient.appendObject(appendObjectRequest);

        // shutdown oss link
        oSSClient.shutdown();

        // save file meta data info database
        // set file path (the file path do not conclude domain info, only save relative path)
        fileDto.setPath(path);
        // save
        fileService.save(fileDto);

        // return file dto with file path in absolute path
        ResponseDto responseDto = new ResponseDto();
        fileDto.setPath(ossDomain + path);
        responseDto.setContent(fileDto);

        return responseDto;
    }

    @PostMapping("/oss-simple")
    public ResponseDto fileUpload(@RequestParam MultipartFile file, String use) throws Exception{

        // file use (course...)
        FileUseEnum useEnum = FileUseEnum.getByCode(use);

        // file random key
        String key = UuidUtil.getShortUuid();

        // file original name
        String fileName = file.getOriginalFilename();

        // .mp4 ...
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // file relative path
        String dir = useEnum.name().toLowerCase();
        String path = dir + "/" + key + "." + suffix;

        // oss
        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // file byte stream
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());

        // file put request
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, path, byteArrayInputStream);

        // do put request
        client.putObject(putObjectRequest);

        // return file dto with file path in absolute path
        ResponseDto responseDto = new ResponseDto();
        FileDto fileDto = new FileDto();
        fileDto.setPath(ossDomain + path);
        responseDto.setContent(fileDto);

        return responseDto;
    }

}
