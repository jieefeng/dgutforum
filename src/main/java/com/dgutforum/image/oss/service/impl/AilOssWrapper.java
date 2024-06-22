package com.dgutforum.image.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.dgutforum.common.util.Md5Util;
import com.dgutforum.image.config.ImageConfig;
import com.dgutforum.image.oss.service.ImageUploader;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;



@Slf4j
@Component
public class AilOssWrapper implements ImageUploader {


    private OSS ossClient;

    private static final int SUCCESS_CODE = 200;

    @Resource
    private ImageConfig imageConfig;


//    public String upload(InputStream input, String fileType) {
//        try {
//            // 创建PutObjectRequest对象。
//            byte[] bytes = StreamUtils.copyToByteArray(input);
//            return upload(bytes, fileType);
//        } catch (OSSException oe) {
//            log.error("Oss rejected with an error response! msg:{}, code:{}, reqId:{}, host:{}", oe.getErrorMessage(), oe.getErrorCode(), oe.getRequestId(), oe.getHostId());
//            return "";
//        } catch (Exception ce) {
//            log.error("Caught an ClientException, which means the client encountered "
//                    + "a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network. {}", ce.getMessage());
//            return "";
//        }
//    }

    public String upload(byte[] bytes, String fileType) {
        try {
            ossClient = new OSSClientBuilder().build(imageConfig.getEndpoint(), imageConfig.getAk(), imageConfig.getSk());
            // 计算md5作为文件名，避免重复上传
            String fileName = Md5Util.encode(bytes);
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);
            fileName = imageConfig.getPrefix() + fileName + "." + fileType;
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(imageConfig.getBucket(), fileName, input);
            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");

            // 上传文件
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            if (SUCCESS_CODE == result.getResponse().getStatusCode()) {
                return "https://"+imageConfig.getBucket()+"."+imageConfig.getEndpoint()+"/"+fileName;
            } else {
                log.error("upload to oss error! response:{}", result.getResponse().getStatusCode());
                return "";
            }
        } catch (OSSException oe) {
            log.error("Oss rejected with an error response! msg:{}, code:{}, reqId:{}, host:{}", oe.getErrorMessage(), oe.getErrorCode(), oe.getRequestId(), oe.getHostId());
            return  "";
        } catch (Exception ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network. {}", ce.getMessage());
            return  "";
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("upload image size:{}", bytes.length);
            }
        }
    }


//
//    public String getMagicNumber(ByteArrayInputStream input) throws IOException {
//        byte[] buffer = new byte[8]; // 读取前8个字节
//        input.read(buffer, 0, buffer.length);
//        StringBuilder magicNumber = new StringBuilder();
//        for (byte b : buffer) {
//            magicNumber.append(String.format("%02X", b));
//        }
//        return magicNumber.toString();
//    }

//    public String getFileType(ByteArrayInputStream input, String fileType) throws IOException {
//        if (fileType != null && !fileType.isEmpty()) {
//            return fileType;
//        }
//
//        String magicNumber = getMagicNumber(input);
//        switch (magicNumber) {
//            case "89504E470D0A1A0A":
//                return "png";
//            case "FFD8FF":
//                return "jpg";
//            case "47494638":
//                return "gif";
//            case "25504446":
//                return "pdf";
//            default:
//                return "unknown";
//        }
//    }




}
