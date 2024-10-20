package com.team1.investsim.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/login")
                .allowedOrigins("*")
                .allowedMethods("POST");

        registry.addMapping("/register")
                .allowedOrigins("*")
                .allowedMethods("POST");

        registry.addMapping("/assets")
                .allowedOrigins("*")
                .allowedMethods("GET");

        registry.addMapping("/asset/**")
                .allowedOrigins("*")
                .allowedMethods("GET");

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST");
    }
}
