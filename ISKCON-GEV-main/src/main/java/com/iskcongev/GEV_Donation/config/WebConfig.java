package com.iskcongev.GEV_Donation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    // CORS is configured in SecurityConfig.corsConfigurationSource()
    // Do NOT add a duplicate WebMvcConfigurer CORS here — it conflicts with Spring Security's CORS filter.
}