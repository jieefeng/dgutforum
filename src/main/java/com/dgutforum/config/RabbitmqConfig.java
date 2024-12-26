package com.dgutforum.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    public static final String ACTIVITY_DIRECT = "dgutforum.activity";

    public static final String PRAISE_QUEUE = "dgutforum.activity.praise";

    public static final String ACTIVITY_BINGING = "addActivity";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    /**
     * activity交换机
     * @return
     */
    @Bean
    public DirectExchange activityExchange(){
        return new DirectExchange(ACTIVITY_DIRECT);
    }

    /**
     * praise队列
     * @return
     */
    @Bean
    public Queue activityQueue(){
        return new Queue(PRAISE_QUEUE);
    }

    /**
     * 绑定点赞的路由键
     * @param activityQueue
     * @param activityExchange
     * @return
     */
    @Bean
    public Binding bindingPraise(Queue activityQueue,DirectExchange activityExchange){
        return BindingBuilder.bind(activityQueue).to(activityExchange).with(ACTIVITY_BINGING);
    }



    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("192.168.80.131");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("itheima");
        connectionFactory.setPassword("123321");
        connectionFactory.setVirtualHost("dgutforum");  // 如果有虚拟主机，设置正确
        return connectionFactory;
    }
}
