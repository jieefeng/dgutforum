package com.dgutforum.image.oss.service;

import com.dgutforum.image.eunms.MediaType;
import io.micrometer.common.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface ImageUploader {

    Set<MediaType> STATIC_IMG_TYPE = new HashSet<>(Arrays.asList(MediaType.IMAGE_PNG, MediaType.IMAGE_JPG, MediaType.IMAGE_GIF));



    /**
     * 文件上传
     *
     * @param input
     * @param fileType
     * @return
     */
    String upload(InputStream input, String fileType);

}
