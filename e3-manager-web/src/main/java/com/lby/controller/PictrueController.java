package com.lby.controller;

import com.lby.common.utils.FastDFSClient;
import com.lby.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: TSF
 * @Description:图片上传控制器
 * @Date: Create in 2018/12/14 16:10
 */
@RestController
public class PictrueController {

    @Value("${IMAGE_SERVER_URL}")
    String IMAGE_SERVER_URL;
    /**
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pic/upload")
    public String uploadFile(MultipartFile uploadFile) throws Exception {
        try {
            //把图片上传到图片服务器
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
            // 取文件扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            String exName = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
            //得到一个图片地址和文件ming
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), exName);
            //补充为完整的url
            url = IMAGE_SERVER_URL + url;
            //封装到map中返回
            Map result = new HashMap<>();
            result.put("error", 0);
            result.put("url", url);
            return JsonUtils.objectToJson(result);
        } catch (Exception e) {
            //封装到map中返回
            Map result = new HashMap<>();
            result.put("error", 1);
            result.put("message", "图片上传失败");
            return JsonUtils.objectToJson(result);
        }
    }

}
