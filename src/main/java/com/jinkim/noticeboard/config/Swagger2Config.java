package com.jinkim.noticeboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class Swagger2Config {

    private static final String API_TITLE = "게시판 API";
    private static final String API_VERSION = "0.0.1";
    private static final String API_DESCRIPTION = "게시판 API 명세서";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
            .apis(RequestHandlerSelectors.basePackage("com.jinbkim.noticeboard.api"))
            .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(API_TITLE).version(API_VERSION)
            .description(API_DESCRIPTION)
            .build();
    }
}
