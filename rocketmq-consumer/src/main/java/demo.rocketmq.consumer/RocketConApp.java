package demo.rocketmq.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-05-25 13:52
 **/
@SpringBootApplication
public class RocketConApp {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RocketConApp.class);
        app.run(args);
    }
}
