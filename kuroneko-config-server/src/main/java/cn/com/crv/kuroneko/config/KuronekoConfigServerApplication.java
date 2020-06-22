package cn.com.crv.kuroneko.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author liuning
 * @date 2010-11-08
 */
@SpringBootApplication
@EnableConfigServer
public class KuronekoConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KuronekoConfigServerApplication.class, args);
	}

}