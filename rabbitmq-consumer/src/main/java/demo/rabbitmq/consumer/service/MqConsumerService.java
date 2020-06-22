package demo.rabbitmq.consumer.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @description: rabbit消费者
 * @author: kuroneko
 * @create: 2020-05-19 22:16
 **/
@Service
public class MqConsumerService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "fanout.A")
    public void process(Map testMessage) {
        System.out.println("FanoutReceiverA消费者收到消息  : " +testMessage.toString());
    }

}
