
package com.example.csye6225.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.csye6225.dao.FilePathRepository;
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


    public String Upload(@RequestParam("file") MultipartFile file, String fileName){
        if (!file.isEmpty()) {
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/";
            String uploadpath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/" + fileName;
            try {
                fileupload(file.getBytes(), path, fileName);
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
                fileupload(file.getBytes(), path, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String U = A.uploadfile(uploadpath, fileName);
            return U;
        }
        return "success";
    }

    public static void fileupload(byte[] file,String filePath,String fileName) throws IOException {
        File targetfile = new File(filePath);
        if(targetfile.exists()) {
            targetfile.mkdirs();
        }

        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        System.out.println("input successfully!");
        out.flush();
        out.close();
    }
}


