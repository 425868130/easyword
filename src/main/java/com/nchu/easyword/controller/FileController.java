package com.nchu.easyword.controller;
import com.nchu.easyword.service.inface.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 2017-10-7 09:42:33
 * 文件上传下载相关 控制器
 */
@CrossOrigin(allowCredentials = "true")
@RestController
public class FileController {
    @Autowired
    @Qualifier("ossFileService")
    FileService fileService;

    /*文件上传*/
    @RequestMapping(value = "/fileUpload")
    public String fileUpload(@RequestPart("file") MultipartFile uploadPicture) throws IOException {
        if (uploadPicture == null) {
            return "";
        }
        return fileService.fileUpload("cache", uploadPicture);
    }


}
