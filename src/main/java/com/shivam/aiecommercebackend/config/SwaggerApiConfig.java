package com.shivam.aiecommercebackend.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
public class SwaggerApiConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("E-Commerce API")
                                .version("v1")
                                .description("This is the backend API documentation for E-Commerce app")
                )
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth",
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                        )
                )
//                .tags(List.of(
//                        new io.swagger.v3.oas.models.tags.Tag()
//                                .name("Auth")
//                                .description("Authentication & Authorization APIs"),
//
//                        new io.swagger.v3.oas.models.tags.Tag()
//                                .name("User")
//                                .description("User profile & account APIs"),
//
//                        new io.swagger.v3.oas.models.tags.Tag()
//                                .name("Product")
//                                .description("Product management APIs"),
//
//                        new io.swagger.v3.oas.models.tags.Tag()
//                                .name("Order")
//                                .description("Order management APIs"),
//
//                        new io.swagger.v3.oas.models.tags.Tag()
//                                .name("Review")
//                                .description("Product review APIs"),
//
//                        new io.swagger.v3.oas.models.tags.Tag()
//                                .name("Return")
//                                .description("Return & refund APIs"),
//
//                        new io.swagger.v3.oas.models.tags.Tag()
//                                .name("Admin")
//                                .description("Admin-only APIs")
//                ))

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
