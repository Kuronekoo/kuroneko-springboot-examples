package demo.rabbitmq.producer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: rabbitmqMq 启动类
 * @author: kuroneko
 * @create: 2020-05-19 17:05
 **/
@SpringBootApplication
public class RabbitMqProducerApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RabbitMqProducerApp.class);
        app.run(args);
    }
}
