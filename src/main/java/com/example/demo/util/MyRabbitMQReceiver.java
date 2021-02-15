package com.example.demo.util;


import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "directQueue")
public class MyRabbitMQReceiver {

    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("MyRabbitMQReceiver消费者收到消息  : " + testMessage.toString());
    }

}
