package com.example.csye6225.test;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

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

    public String uploadfile(String filepath, String remoteFileName){

        File tempFile = new File(filepath);
        try {
            String bucketPath = bucketName + "/upload" ;
            s3.putObject(new PutObjectRequest(bucketPath, remoteFileName, tempFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, remoteFileName);
            URL url = s3.generatePresignedUrl(urlRequest);
            return url.toString();
        } catch (AmazonServiceException ase) {
            ase.printStackTrace();
        } catch (AmazonClientException ace) {
            ace.printStackTrace();
        }
        return null;
    }

    public void deletefile(String object_name)
    {
        final String USAGE = "\n" +
                "To run this example, supply the name of an S3 bucket and object\n" +
                "name (key) to delete.\n" +
                "\n" +
                "Ex: DeleteObject <bucketname> <objectname>\n";

        String bucket_name = bucketName;
        String key_name = object_name;

        System.out.format("Deleting object %s from S3 bucket: %s\n", key_name,
                bucket_name);
        // final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        try {
            s3.deleteObject(bucket_name, key_name);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Done!");
    }


    public String updatefile(String filepath,String filename)
    {
        final String USAGE = "\n" +
                "To run this example, supply the name of an S3 bucket and a file to\n" +
                "upload to it.\n" +
                "\n" +
                "Ex: PutObject <bucketname> <filename>\n";
        String bucket_name = bucketName;
        String file_path = filepath;
        String key_name = filename;

        System.out.format("Uploading %s to S3 bucket %s...\n", file_path, bucket_name);
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        try {
            s3.putObject(bucket_name, key_name, new File(file_path));

            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, key_name);
            URL url = s3.generatePresignedUrl(urlRequest);
            return url.toString();

        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Done!");
        return "error";
    }
}

