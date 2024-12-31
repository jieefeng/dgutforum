package com.dgutforum.image.service;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
//
//    /**
//     * 上传图片
//     * @param request
//     * @return
//     */
//    String saveImg(HttpServletRequest request);

    public String saveImage(String image);

    public String saveImage(MultipartFile file) throws IOException;

}
