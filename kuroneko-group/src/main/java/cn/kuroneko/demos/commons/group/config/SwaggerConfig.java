package cn.kuroneko.demos.commons.group.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangyingkai
 */
@Configuration
@EnableSwagger2
@Profile({ "default", "dev", "test", "sit", "uat" })
public class SwaggerConfig {
    /**
     * swagger扫描的包路径
     */
    private static final String SWAGGER_BASE_PACKAGE = "cn.kuroneko.demos.group";

    /**
     * 访问地址 http://127.0.0.1:18103/swagger-ui.html
     * @return
     */
    @Bean
    public Docket api() {

        //在swagger上加上对请求头的要求
        List<Parameter> pars = new ArrayList<>();
        pars.add(new ParameterBuilder().name("token").description("令牌,/user/login 接口无须填写，其它接口必填。")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build());
        pars.add(new ParameterBuilder().name("userId").description("用户ID,/user/login 接口无须填写，其它接口必填。")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build());

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_BASE_PACKAGE))
                .paths(PathSelectors.any()).build().globalOperationParameters(pars);
    }

}
