package cn.kuroneko.demos.user;

import cn.kuroneko.demos.exception.KuronekoException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-20 16:16
 **/
@EnableFeignClients
@SpringBootApplication
@MapperScan(basePackages = "cn.kuroneko.demos.user.mapper")
public class UserApp {
    public static void main(String[] args) {
        KuronekoException.BIZ_CODE = "USER";
        SpringApplication.run(UserApp.class,args);
    }
}
