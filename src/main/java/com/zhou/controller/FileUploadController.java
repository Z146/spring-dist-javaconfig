package com.zhou.controller;

import com.zhou.common.FastDFSClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class FileUploadController {


    /**
     * 将文件上传到本地
     * @param file 文件
     * @return 成功信息
     * @throws IOException 异常
     */
    @PostMapping(value = "/fileupload")
    public String fileupload(MultipartFile file) throws IOException {
        String oldName = file.getOriginalFilename();
        file.transferTo(new File("data/"+oldName));
        return "文件上传成功";
    }

    /**
     * 上传图片到远程服务器 fastdfs
     * @param file 文件
     * @return 提示信息
     */
    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        //获取文件的全名称
        String oldFilename = file.getOriginalFilename();

        //获取文件拓展名
        String extName = oldFilename.substring(oldFilename.lastIndexOf(".") + 1);

        try {
            FastDFSClient client = new FastDFSClient("classpath:fastDFS.conf");

            String fileId = client.uploadFile(file.getBytes(), extName);
            //获取已上传文件的全路径
            String url = "http://47.100.16.153/" + fileId;

            return "上传成功："+url;
        } catch (Exception e) {
            e.printStackTrace();
            return "出错了！";
        }
    }
}
