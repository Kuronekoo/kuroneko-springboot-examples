package cn.com.crv.vwop.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author
 * @date 2019-11-19`
 */
@SpringBootApplication(
        exclude = { DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class KuronekoGatewayApplication {

    public static void main(String[] args) {

        SpringApplication.run(KuronekoGatewayApplication.class, args);
    }

}
