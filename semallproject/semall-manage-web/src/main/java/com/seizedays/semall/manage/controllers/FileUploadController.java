package com.seizedays.semall.manage.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.seizedays.semall.webutils.FileUtil;
import com.seizedays.semall.utils.SnowFlake;

@RestController
@CrossOrigin
public class FileUploadController {

    SnowFlake snowFlake = new SnowFlake(5L, 5L);

    @Value("${boot.filePath}")
    private String uploadPrefix; //文件保存路径前缀

    @Value("${server.port}")
    private String servePort;

    @Value("${ipAddress.value}")
    private String ipAdress;

    @RequestMapping("/fileUpload")
    public String getFilePath(@RequestParam("file") MultipartFile file) {

        //判断上传文件格式
        String fileType = file.getContentType();
        if ("image/jpg".equals(fileType) || "image/png".equals(fileType) || "image/jpeg".equals(fileType)) {

            //获取原始文件名
            String fileName = file.getOriginalFilename();

            //获取上传文件名后缀
            String suffixName = fileName.substring(fileName.lastIndexOf("."));

            //自定义文件名为用户的uid
            fileName = "/spuImage/" + snowFlake.nextId() + suffixName;


            String realPath = uploadPrefix + fileName;
            if (FileUtil.upload(file, realPath)) {
                //文件存放的相对路径(一般存放在数据库用于img标签的src)
                String uploadPath = "http://" + ipAdress+ ":" + servePort + "/images" + fileName;
                System.out.println(uploadPath);
                return uploadPath;
            }

        }
        return null;
    }
}
