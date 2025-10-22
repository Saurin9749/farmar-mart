package com.farmermart.orderservice.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI orderServiceOpenAPI() {
        return new OpenAPI().info(new Info().title("FarmerMart Order Service API").description("APIs for managing orders, products, and users").version("1.0.0"));
    }
}
