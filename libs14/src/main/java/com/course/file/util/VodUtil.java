package com.course.file.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetMezzanineInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetMezzanineInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.InputStream;

public class VodUtil {
    private static final String REGION_ID = "cn-shanghai";

    private static final Long VOD_CATEGORY_ID = 1000204016L;
    private static final String VOD_TEMPLATE_GROUP_ID = "eba00bc94de3819b6c65b6c0f3e4d1e3";


    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException {

        // region
        String regionId = REGION_ID;

        // default profile
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);

        // client
        DefaultAcsClient client = new DefaultAcsClient(profile);

        return client;
    }

    public static CreateUploadVideoResponse createUploadVideoResponse(DefaultAcsClient vodClient, String fileName) throws ClientException {

        // create upload video request
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();

        request.setFileName(fileName);
        request.setTitle(fileName);

        request.setCateId(VOD_CATEGORY_ID);
        request.setTemplateGroupId(VOD_TEMPLATE_GROUP_ID);

        request.setReadTimeout(1000);
        request.setConnectTimeout(1000);

        // response
        CreateUploadVideoResponse createUploadVideoResponse  = vodClient.getAcsResponse(request);

        return createUploadVideoResponse;
    }

    public static OSSClient initOssClient(JSONObject uploadAuth, JSONObject uploadAddress){

        // endpoint
        String endpoint = uploadAddress.getString("Endpoint");

        // credentials
        String accessKeyId = uploadAuth.getString("AccessKeyId");
        String accessKeySecret = uploadAuth.getString("AccessKeySecret");
        String securityToken = uploadAuth.getString("SecurityToken");

        // oss client
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret, securityToken);
        OSSClient ossClient = new OSSClient(endpoint, credentialsProvider, clientConfiguration);

        // new OSSClient(endpoint, accessKeyId, accessKeySecret, securityToken)
        return ossClient;
    }

    public static void uploadLocalFile(OSSClient client, JSONObject uploadAddress, InputStream inputStream){

        String bucketName = uploadAddress.getString("Bucket");
        String objectName = uploadAddress.getString("FileName");

        // put object
        client.putObject(bucketName, objectName, inputStream);
    }

    public static void uploadLocalFile(OSSClient client, JSONObject uploadAddress, String localFile){

        String bucketName = uploadAddress.getString("Bucket");
        String objectName = uploadAddress.getString("FileName");
        File file = new File(localFile);

        // put object
        client.putObject(bucketName, objectName, file);
    }

     public static RefreshUploadVideoResponse refreshUploadVideo(DefaultAcsClient vodClient) throws ClientException{

        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();

        request.setAcceptFormat(FormatType.JSON);
        request.setVideoId("VideoId");

        request.setReadTimeout(1000);
        request.setConnectTimeout(1000);

        return vodClient.getAcsResponse(request);
    }

     public static GetMezzanineInfoResponse getMezzanineInfo(DefaultAcsClient client, String videoId) throws Exception{

         GetMezzanineInfoRequest request = new GetMezzanineInfoRequest();

         request.setVideoId(videoId);
         request.setAuthTimeout(3600L);

        return client.getAcsResponse(request);
     }

     public static GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client, String videoId) throws Exception{

        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

        request.setVideoId(videoId);

        return client.getAcsResponse(request);
     }

    public static void main(String[] argv) {

        String accessKeyId = "LTAI4FnmXZVs9Pufn8kt2whV";
        String accessKeySecret = "yQqZJ0WGnyAxaT9Gy1mKIJXY68F6A6";
        String localFile = "D:\\imooc\\course\\workspace\\course\\admin\\public\\static\\image\\小节视频\\test.mp4";

        try {

            DefaultAcsClient vodClient = initVodClient(accessKeyId, accessKeySecret);
            String fileName = "test.mp4";
            CreateUploadVideoResponse createUploadVideoResponse = createUploadVideoResponse(vodClient, fileName);

            String videoId = createUploadVideoResponse.getVideoId();
            JSONObject uploadAuth = JSONObject.parseObject(
                    Base64.decodeBase64(createUploadVideoResponse.getUploadAuth()), JSONObject.class);
            JSONObject uploadAddress = JSONObject.parseObject(
                    Base64.decodeBase64(createUploadVideoResponse.getUploadAddress()), JSONObject.class);

            OSSClient ossClient = initOssClient(uploadAuth, uploadAddress);

            uploadLocalFile(ossClient, uploadAddress, localFile);

            GetMezzanineInfoResponse response = new GetMezzanineInfoResponse();
            response = getMezzanineInfo(vodClient, videoId);

        } catch (Exception e) {

        }
    }


}
