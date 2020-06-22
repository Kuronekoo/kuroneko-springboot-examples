package demo.rabbitmq.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: rabbitMq消费者
 * @author: kuroneko
 * @create: 2020-05-19 21:46
 **/
@SpringBootApplication
public class RabbitMqConsumerApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RabbitMqConsumerApp.class);
        app.run(args);
    }
}
