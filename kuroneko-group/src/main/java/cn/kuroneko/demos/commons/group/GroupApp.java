package cn.kuroneko.demos.commons.group;

import cn.kuroneko.demos.commons.exception.KuronekoException;
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
@MapperScan(basePackages = "cn.kuroneko.demos.group.mapper")
public class GroupApp {
    public static void main(String[] args) {

        KuronekoException.BIZ_CODE = "GROUP";
        SpringApplication.run(GroupApp.class,args);
    }
}
