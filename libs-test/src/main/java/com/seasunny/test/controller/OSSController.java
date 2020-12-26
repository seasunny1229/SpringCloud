package com.seasunny.test.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.AppendObjectRequest;
import com.aliyun.oss.model.GenericRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutBucketImageRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.seasunny.test.dto.ResponseDto;
import com.seasunny.test.dto.UploadStringDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/oss")
public class OSSController {


    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.domain}")
    private String ossDomain;

    @Value("${oss.bucket}")
    private String bucket;


    @PostMapping("/upload/string")
    public ResponseDto upload(@RequestBody UploadStringDto uploadStringDto){

        // oss client
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // dto
        String key = uploadStringDto.getKey();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(uploadStringDto.getContent().getBytes());

        OSSObjectSummary ossObjectSummary = getOSSObjectSummary(ossClient, key);

        // upload to OSS
        if(uploadStringDto.isAppend()){
            AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucket, key, byteArrayInputStream);
            appendObjectRequest.setPosition(ossObjectSummary == null ? 0 : ossObjectSummary.getSize());
            ossClient.appendObject(appendObjectRequest);
        }
        else {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, byteArrayInputStream);
            ossClient.putObject(putObjectRequest);
        }

        // shutdown
        ossClient.shutdown();

        return new ResponseDto<>();
    }


    @PostMapping("download/string")
    public ResponseDto download(@RequestBody String key) throws IOException {

        // oss client
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // request
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);

        // result
        OSSObject ossObject = ossClient.getObject(getObjectRequest);

        // read
        InputStream inputStream = ossObject.getObjectContent();
        byte[] bytes = new byte[inputStream.available()];
        int n = inputStream.read(bytes);
        String str = new String(bytes, Charset.forName("UTF-8"));

        // shutdown
        ossClient.shutdown();

        ResponseDto<String> responseDto = new ResponseDto<>();
        responseDto.setContent(str);

        return responseDto;
    }

    @PostMapping("get/summary")
    public ResponseDto getObjectSummary(@RequestBody String key){

        // oss client
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        ResponseDto<OSSObjectSummary> responseDto = new ResponseDto<>();
        responseDto.setContent(getOSSObjectSummary(ossClient, key));

        return responseDto;
    }

    private OSSObjectSummary getOSSObjectSummary(OSS ossClient, String key){
        OSSObjectSummary ossObjectSummary = null;
        ObjectListing objectListing = ossClient.listObjects(bucket);
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            if(objectSummary.getKey().equals(key)){
                ossObjectSummary = objectSummary;
            }
        }
        return ossObjectSummary;
    }

}
