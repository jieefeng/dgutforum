package com.dgutforum.image.controller;

import com.dgutforum.common.result.ResVo;
import com.dgutforum.common.result.Result;
import com.dgutforum.common.result.eunms.StatusEnum;
import com.dgutforum.common.util.AliOssUtil;
import com.dgutforum.image.vo.ImageDTO;
import com.dgutforum.image.service.ImageService;
import com.dgutforum.image.vo.ImageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequestMapping("/image")
@RestController
@Slf4j
@Tag(name = "图片相关接口")
public class imageController {

    @Resource
    private ImageService imageService;
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 保存图片到OSS
     * @param image
     * @return
     */
    @PostMapping("/4")
    @Operation(summary = "保存图片到OSS")
    public Result upload(@RequestBody ImageDTO image) {
        ImageVo imageVo = new ImageVo();
        try {
            String imagePath = imageService.saveImage(image.getImage());
            imageVo.setImagePath(imagePath);
        } catch (Exception e) {
            log.error("save upload file error!", e);
            return Result.error("图片上传失败！");
        }
        return Result.success(imageVo);
    }

    @PostMapping("/upload")
    public Result uploadImage(@RequestParam("file") MultipartFile file) {
        log.info("文件上传,file={}", file);

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID().toString() + extension;

            String filepath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filepath);
        } catch (IOException e) {
            log.error("文件上传失败：{}",e);
            throw new RuntimeException(e);
        }
    }

}
