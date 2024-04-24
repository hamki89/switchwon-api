package com.switchwon.switchwonapi.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("스위치원 과제 API")
                        .description("과제 관련 3가지 API를 구현하였습니다.")
                        .version("1.0.0"));
    }
}
