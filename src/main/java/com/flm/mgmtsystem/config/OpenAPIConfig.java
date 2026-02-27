package com.flm.mgmtsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI MultiUserProjectAndTaskMgmtSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Multi User Project and Task Management System API")
                        .description("Management system for Assigning and reassigning tasks and project activation, deactivation, according to the user ROLE")
                        .version("v1.0"));
    }
}
