package com.dgutforum.image.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "image")
@Data
public class ImageConfig {

    /**
     * 上传文件前缀路径
     */
    private String prefix;
    /**
     * oss类型
     */
    private String type;
    /**
     * 下面几个是oss的配置参数
     */
    private String endpoint;
    private String ak;
    private String sk;
    private String bucket;
    private String host;
}
