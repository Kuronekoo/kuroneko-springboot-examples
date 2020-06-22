package cn.kuroneko.demos.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-19 11:57
 **/
@SpringBootApplication
@MapperScan(basePackages = "cn.kuroneko.demos.mybatis.mapper")
public class MybatisAnnoApp {
    public static void main(String[] args) {
        SpringApplication.run(MybatisAnnoApp.class,args);
    }
}
