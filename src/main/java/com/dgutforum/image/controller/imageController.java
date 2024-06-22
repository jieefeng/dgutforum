package com.dgutforum.image.controller;

import com.dgutforum.common.result.ResVo;
import com.dgutforum.common.result.eunms.StatusEnum;
import com.dgutforum.image.vo.ImageDTO;
import com.dgutforum.image.service.ImageService;
import com.dgutforum.image.vo.ImageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/image")
@RestController
@Slf4j
public class imageController {

    @Resource
    private ImageService imageService;


//    /**
//     * 图片上传
//     *
//     * @return
//     */
//
//    @RequestMapping(path = "upload")
//    public ResVo<ImageVo> upload(HttpServletRequest request) {
//        ImageVo imageVo = new ImageVo();
//        try {
//            String imagePath = imageService.saveImg(request);
//            imageVo.setImagePath(imagePath);
//        } catch (Exception e) {
//            log.error("save upload file error!", e);
//            return ResVo.fail(StatusEnum.UPLOAD_PIC_FAILED);
//        }
//        return ResVo.ok(imageVo);
//    }


    /**
     * 保存图片到OSS
     * @param image
     * @return
     */
    @PostMapping("save")
    public ResVo<ImageVo> upload(@RequestBody ImageDTO image) {
        ImageVo imageVo = new ImageVo();
        try {
            String imagePath = imageService.saveImage(image.getImage());
            imageVo.setImagePath(imagePath);
        } catch (Exception e) {
            log.error("save upload file error!", e);
            return ResVo.fail(StatusEnum.UPLOAD_PIC_FAILED);
        }
        return ResVo.ok(imageVo);
    }

}
