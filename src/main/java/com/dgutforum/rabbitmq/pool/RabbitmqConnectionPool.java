package com.dgutforum.rabbitmq.pool;

import com.dgutforum.config.RabbitmqConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RabbitmqConnectionPool {

    private BlockingQueue<RabbitmqConnection> pool;

    @Resource
    private RabbitmqConfig rabbitmqConfig;

    @PostConstruct
    public void init() {
        // 使用rabbitmqConfig中配置的值来初始化连接池
        initRabbitmqConnectionPool(
                rabbitmqConfig.getHost(),
                rabbitmqConfig.getPort(),
                rabbitmqConfig.getUsername(),
                rabbitmqConfig.getPassword(),
                rabbitmqConfig.getVirtualHost(),
                rabbitmqConfig.getPoolSize()
        );
    }


    public void initRabbitmqConnectionPool(String host, int port, String userName, String password,
                                             String virtualhost,
                                           Integer poolSize) {
        pool = new LinkedBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            pool.add(new RabbitmqConnection(host, port, userName, password, virtualhost));
        }
    }

    public RabbitmqConnection getConnection() throws InterruptedException {
        return pool.take();
    }

    public void returnConnection(RabbitmqConnection connection) {
        pool.add(connection);
    }

    public void close() {
        pool.forEach(RabbitmqConnection::close);
    }
}
