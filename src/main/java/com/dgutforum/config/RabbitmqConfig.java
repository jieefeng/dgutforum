package com.dgutforum.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Component
public class RabbitmqConfig {

    private String host;

    private int port;

    private String username;

    private String password;

    private String virtualHost;

    private int poolSize;

}
