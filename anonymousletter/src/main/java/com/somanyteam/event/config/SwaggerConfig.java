package com.somanyteam.event.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger3配置文件
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {

        //设置要显示的swagger环境
//        Profiles profile = Profiles.of("dev","test","prod");
//
//        //通过environment.acceptsProfiles判断是否处在自己设定的环境当中
//        boolean flag = environment.acceptsProfiles(profile);

        return new Docket(DocumentationType.OAS_30)
                // 注入文档信息
                //.enable(flag)
                .apiInfo(apiInfo())
                .select()
                // 对所有api进行监控
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 对根下所有路径进行监控
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 配置文档信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("匿名提问箱")
                .description("好几个队")
                .build();
    }

}
