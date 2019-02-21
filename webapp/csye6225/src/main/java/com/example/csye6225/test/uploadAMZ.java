package com.example.csye6225.test;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class uploadAMZ {

    static AmazonS3 s3;
    static String AWS_ACCESS_KEY = "AKIAI2KUSZUVMVONE3YQ"; // 【你的 access_key】
    static String AWS_SECRET_KEY = "2BW4ptBxjEvDs1pM5kgWTcy8QXI4IcJrMNlA/und"; // 【你的 aws_secret_key】

    String bucketName = "cf-templates-eojwssbxp6cn-us-east-1"; // 【你 bucket 的名字】 # 首先需要保证 s3 上已经存在该存储桶

    static {
        s3 = new AmazonS3Client(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY));
        s3.setRegion(Region.getRegion(Regions.US_EAST_1)); // 此处根据自己的 s3 地区位置改变
    }

    public String uploadToS3(File tempFile, String remoteFileName) throws IOException {
        try {
            String bucketPath = bucketName + "/upload" ;
            s3.putObject(new PutObjectRequest(bucketPath, remoteFileName, tempFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, remoteFileName);
            URL url = s3.generatePresignedUrl(urlRequest);

            System.out.println(urlRequest);
            System.out.println(url);

            return url.toString();
        } catch (AmazonServiceException ase) {
            ase.printStackTrace();
        } catch (AmazonClientException ace) {
            ace.printStackTrace();
        }
        return null;
    }

    public String demo(String  path, String filename) throws IOException {
        File uploadFile = new File(path);
        String uploadKey = filename;
        String url = uploadToS3(uploadFile,uploadKey);
        return url;
    }
}

