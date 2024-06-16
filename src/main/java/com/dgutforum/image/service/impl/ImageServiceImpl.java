package com.dgutforum.image.service.impl;

import com.dgutforum.common.exception.ExceptionUtil;
import com.dgutforum.common.result.eunms.StatusEnum;
import com.dgutforum.image.eunms.MediaType;
import com.dgutforum.image.oss.service.ImageUploader;
import com.dgutforum.image.service.ImageService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Resource
    private ImageUploader imageUploader;


    @Override
    public String saveImg(HttpServletRequest request) {
        MultipartFile file = null;
        if (request instanceof MultipartHttpServletRequest) {
            file = ((MultipartHttpServletRequest) request).getFile("image");
        }
        if (file == null) {
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "缺少需要上传的图片");
        }

        // 目前只支持 jpg, png, webp 等静态图片格式
        String fileType = validateStaticImg(file.getContentType());
        if (fileType == null) {
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "图片只支持png,jpg,gif");
        }

        try {
            return imageUploader.upload(file.getInputStream(), fileType);
        } catch (IOException e) {
            log.error("Parse img from httpRequest to BufferedImage error! e:", e);
            throw ExceptionUtil.of(StatusEnum.UPLOAD_PIC_FAILED);
        }
    }

    /**
     * 图片格式校验
     *
     * @param mime
     * @return
     */
    private String validateStaticImg(String mime) {
        for (MediaType type : ImageUploader.STATIC_IMG_TYPE) {
            if (type.getMime().equals(mime)) {
                return type.getExt();
            }
        }
        return null;
    }

}
