package com.example.csye6225.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
public class FileController {

    @Autowired
    private FilePathService filePathService;
    // 传入的参数file是我们指定的文件
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        return filePathService.Upload(file);
    }
}


