package com.course.file.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetMezzanineInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.course.common.dto.ResponseDto;
import com.course.file.dto.FileDto;
import com.course.file.enums.FileUseEnum;
import com.course.file.service.FileService;
import com.course.file.util.Base64ToMultipartFile;
import com.course.file.util.VodUtil;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class VodController {

    // logger
    private static final Logger LOG = LoggerFactory.getLogger(UploadController.class);

    // business name
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

    @PostMapping("/vod")
    public ResponseDto file(@RequestBody FileDto fileDto) throws Exception{

        // base file info
        // file use (course....)
        String use = fileDto.getUse();

        // random generated key
        String key = fileDto.getKey();

        // .mp4
        String suffix = fileDto.getSuffix();

        // file meta info
        Integer shardIndex = fileDto.getShardIndex();
        Integer shardSize = fileDto.getShardSize();

        // multipart file
        String shardBase64 = fileDto.getShard();
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);

        // file use enum (course...)
        // file dir (primary dir)
        FileUseEnum useEnum = FileUseEnum.getByCode(use);
        String dir = useEnum.name().toLowerCase();

        // file relative path
        // course/6sfSqfOwzmik4A4icMYuUe.mp4
        String path = new StringBuffer(dir)
                .append("/")
                .append(key)
                .append(".")
                .append(suffix)
                .toString();


        String vod = "";
        String fileUrl = "";

        try {

            // vod client
            DefaultAcsClient vodClient = VodUtil.initVodClient(accessKeyId, accessKeySecret);

            // create upload video response
            CreateUploadVideoResponse createUploadVideoResponse = VodUtil.createUploadVideoResponse(vodClient, path);

            // video id
            vod = createUploadVideoResponse.getVideoId();

            // decode upload auth
            byte[] decodedUploadAuth =  Base64.decodeBase64(createUploadVideoResponse.getUploadAuth());
            JSONObject uploadAuth = JSONObject.parseObject(decodedUploadAuth, JSONObject.class);

            // decode upload address
            byte[] decodedUploadAddress =  Base64.decodeBase64(createUploadVideoResponse.getUploadAddress());
            JSONObject uploadAddress = JSONObject.parseObject(decodedUploadAddress, JSONObject.class);

            // oss client
            OSSClient ossClient = VodUtil.initOssClient(uploadAuth, uploadAddress);

            // upload local file
            VodUtil.uploadLocalFile(ossClient, uploadAddress, shard.getInputStream());

            // response
            GetMezzanineInfoResponse response = VodUtil.getMezzanineInfo(vodClient, vod);

            // get file url
            fileUrl = response.getMezzanine().getFileURL();

            // oss shutdown
            ossClient.shutdown();

        }catch (Exception e){
            e.printStackTrace();
        }

        // save file meta info
        fileDto.setPath(path);
        fileDto.setVod(vod);
        fileService.save(fileDto);

        // response
        ResponseDto responseDto = new ResponseDto();
        fileDto.setPath(fileUrl);
        responseDto.setContent(fileDto);

        return responseDto;
    }


    @GetMapping(value = "/get-auth/{vod}")
    public ResponseDto getAuth(@PathVariable("vod") String vod) throws ClientException{

        ResponseDto responseDto = new ResponseDto();

        // acs client
        DefaultAcsClient client = VodUtil.initVodClient(accessKeyId, accessKeySecret);

        // video play auth response
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        try {

            // get video play auth response
            response = VodUtil.getVideoPlayAuth(client, vod);

            responseDto.setContent(response.getPlayAuth());

        }catch (Exception e){
            e.printStackTrace();
        }

        return responseDto;
    }
}
