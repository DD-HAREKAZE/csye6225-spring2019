package com.example.csye6225.controller;


import com.example.csye6225.FilePath;
import com.example.csye6225.uploadAMZ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FilePathService {


    @Autowired
    private FilePathRepository filePathRepository;

    public String Upload(@RequestParam("file") MultipartFile file) throws IOException {
        if(!file.isEmpty()) {

            String fileName = file.getOriginalFilename();


            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/";
            String uploadpath=path+fileName;

            try {

                FileUtil.fileupload(file.getBytes(), path, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }


            uploadAMZ A = new uploadAMZ();
            String amzurl = A.demo(uploadpath,fileName);

            FilePath biaopath = new FilePath();
            biaopath.setPath(amzurl);
            filePathRepository.save(biaopath);
        }
        return "success";

    }
}