package com.dgutforum.image.service.impl;

import com.dgutforum.image.oss.service.ImageUploader;
import com.dgutforum.image.service.ImageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Resource
    private ImageUploader imageUploader;


//    @Override
//    public String saveImg(HttpServletRequest request) {
//        MultipartFile file = null;
//        if (request instanceof MultipartHttpServletRequest) {
//            file = ((MultipartHttpServletRequest) request).getFile("image");
//        }
//        if (file == null) {
//            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "缺少需要上传的图片");
//        }
//
//        // 目前只支持 jpg, png, webp 等静态图片格式
//        String fileType = validateStaticImg(file.getContentType());
//        if (fileType == null) {
//            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "图片只支持png,jpg,gif");
//        }
//
//        try {
//            return imageUploader.upload(file.getInputStream(), fileType);
//        } catch (IOException e) {
//            log.error("Parse img from httpRequest to BufferedImage error! e:", e);
//            throw ExceptionUtil.of(StatusEnum.UPLOAD_PIC_FAILED);
//        }
//    }
//
//    /**
//     * 图片格式校验
//     *
//     * @param mime
//     * @return
//     */
//    private String validateStaticImg(String mime) {
//        for (MediaType type : ImageUploader.STATIC_IMG_TYPE) {
//            if (type.getMime().equals(mime)) {
//                return type.getExt();
//            }
//        }
//        return null;
//    }

    /**
     * 根据base64转换成的字节数据获取图片类型
     * @param imageData
     * @return
     */
    public static String detectImageType(byte[] imageData) {
        // 判断图片类型
        if (imageData.length < 3) {
            return null; // 数据长度不足，无法确定类型
        }

        // JPEG 文件头：FF D8 FF
        if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8 && imageData[2] == (byte) 0xFF) {
            return "JPEG";
        }

        // PNG 文件头：89 50 4E 47
        if (imageData[0] == (byte) 0x89 && imageData[1] == (byte) 0x50 && imageData[2] == (byte) 0x4E
                && imageData[3] == (byte) 0x47) {
            return "PNG";
        }

        // GIF 文件头：47 49 46
        if (imageData[0] == (byte) 0x47 && imageData[1] == (byte) 0x49 && imageData[2] == (byte) 0x46) {
            return "GIF";
        }

        // 其他未知格式
        return "Unknown";
    }





    @Override
    public String saveImage(String image) {
        // 解码 Base64 字符串
        byte[] imageBytes = new byte[0];
        imageBytes = Base64.getMimeDecoder().decode(image);
        String fileType = detectImageType(imageBytes);
        // 保存图片到alioss
        String filePath = imageUploader.upload(imageBytes, fileType);
        return filePath;
    }
}
