package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);

        try {
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            // 截取文件后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 构造新文件名
            String objectName = java.util.UUID.randomUUID().toString() + extension;

            // 本地存储路径
            String directoryPath = "E:/images/";
            java.io.File dir = new java.io.File(directoryPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 完整文件路径
            String filePath = directoryPath + objectName;

            // 将文件保存到指定位置
            file.transferTo(new java.io.File(filePath));

            // 返回文件路径（实际生产中通常返回可访问的URL，这里返回本地路径供测试）
            return Result.success("http://localhost:8080/images/" + objectName);

        } catch (java.io.IOException e) {
            log.error("文件上传失败：{}", e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }
}