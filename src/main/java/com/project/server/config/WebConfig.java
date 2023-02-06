package com.project.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .securityContexts(Stream.of(securityContext()).collect(Collectors.toList()))
                .securitySchemes(Collections.singletonList(securitySchema()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.deu.synabro"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }
}
