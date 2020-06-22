package rocketmq.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-05-25 13:53
 **/
@SpringBootApplication
public class RocketProApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RocketProApp.class);
        app.run(args);
    }
}
