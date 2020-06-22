package cn.kuroneko.demos.jpa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-19 11:57
 **/
@SpringBootApplication
public class JpaAnnoApp {
    public static void main(String[] args) {
        SpringApplication.run(JpaAnnoApp.class,args);
    }
}
