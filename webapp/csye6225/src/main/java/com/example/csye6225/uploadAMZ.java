package com.example.csye6225;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
//@athur yifu xu
//test: put something local to AWS S3
public class uploadAMZ {

    static AmazonS3 s3;
    static String AWS_ACCESS_KEY = "AKIAI2KUSZUVMVONE3YQ";
    static String AWS_SECRET_KEY = "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";

    String bucketName = "cf-templates-eojwssbxp6cn-us-east-1";

    static {
        s3 = new AmazonS3Client(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY));
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
    }

    public String uploadToS3(File tempFile, String remoteFileName) throws IOException {
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


    public String demo(String  path, String filename) throws IOException {
        File uploadFile = new File(path);
        String uploadKey = filename;
        String url = uploadToS3(uploadFile,uploadKey);
        return url;
    }
}