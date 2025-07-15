package com.todo.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // autorise toutes les routes
                .allowedOrigins("http://localhost:3000") // autorise React
                .allowedMethods("*") // autorise toutes les méthodes : GET, POST, etc.
                .allowedHeaders("*"); // autorise tous les headers
    }
}
