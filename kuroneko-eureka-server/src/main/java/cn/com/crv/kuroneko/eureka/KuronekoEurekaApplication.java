package cn.com.crv.kuroneko.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author liuning
 */
@SpringBootApplication
@EnableEurekaServer
public class KuronekoEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KuronekoEurekaApplication.class, args);
	}

}
