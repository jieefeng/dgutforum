package com.dgutforum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class DgutforumApplication {

    public static void main(String[] args) {
        SpringApplication.run(DgutforumApplication.class, args);
    }

}
