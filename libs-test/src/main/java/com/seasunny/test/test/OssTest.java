package com.seasunny.test.test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.ListBucketsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class OssTest {

    private static String endpoint = "oss-cn-shanghai.aliyuncs.com";
    private static String accessKeyId = "LTAI4G5mbZ53e68VgdsJvNn9";
    private static String accessKeySecret = "mLvsGLELzRr0EpT4lknDHvb4YRJTJW";
    private static String bucketName = "seasunny1229-training";
    private static String key = "<key>";


    public static void main(String[] args) throws IOException {


        /*
         * Constructs a client instance with your account for accessing OSS
         */
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        /*
         * List the buckets in your account
         */
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        listBucketsRequest.setMaxKeys(500);

        for (Bucket bucket : ossClient.listBuckets()) {
            System.out.println(" - " + bucket.getName());
        }

        /*
         * List objects in your bucket by prefix
         */
        System.out.println("Listing objects");
        ObjectListing objectListing = ossClient.listObjects(bucketName);
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " +
                    "(size = " + objectSummary.getSize() + ")");
        }
        System.out.println();


        /*
         * Upload an object to your bucket
         */
        System.out.println("Uploading a new object to OSS from a file\n");
        ossClient.putObject(new PutObjectRequest(bucketName, "training1/2.txt", createSampleFile()));

    }

    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("oss-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.write("0123456789011234567890\n");
        writer.close();

        return file;
    }

}
