
package com.example.csye6225.controller;


import java.io.IOException;

import com.example.csye6225.FilePath;
import com.example.csye6225.test.uploadAMZ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilePathService {


    @Autowired
    private FilePathRepository filePathRepository;
    uploadAMZ A = new uploadAMZ();

    public String Upload(@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/";
            String uploadpath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/" + fileName;
            try {
                FileUtil.fileupload(file.getBytes(), path, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String U = A.uploadfile(uploadpath, fileName);
            return U;
        }
        return "success";
    }

    public void delete(String filename) {

        A.deletefile(filename);

    }

    public String update(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();

            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/";
            String uploadpath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/" + fileName;
            try {
                FileUtil.fileupload(file.getBytes(), path, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String U = A.uploadfile(uploadpath, fileName);
            return U;
        }
        return "success";
    }
}


