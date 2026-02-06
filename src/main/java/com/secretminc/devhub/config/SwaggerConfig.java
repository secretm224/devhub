package com.secretminc.devhub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DevHub API")
                        .description("DevHub 프로젝트 API 문서")
                        .version("v1.0.0"));
    }
}
