package com.iskcongev.GEV_Donation.config;

import java.util.Arrays; // Your custom JWT filter might still be useful

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // NEW
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter; // NEW
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter; // NEW
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.iskcongev.GEV_Donation.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // You might remove this if JwtAuthenticationFilter is for your internal JWTs and you're moving to Firebase ID Tokens
    private final JwtAuthenticationFilter jwtFilter;

    @Value("${cors.allowed-origins}")
    private String[] corsAllowedOrigins;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) { // Adjust constructor based on filters used
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API-only apps
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // APIs are stateless
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/",
                        "/cause-categories/**",
                        "/causes/**",
                        "/donor/**",
                        "/uploads/**",
                        "/options/**",
                        "/options-groups/**",
                        "/error",
                        "/verifyDonor",
                        "/verifyAdmin",
                        "/verify-id-token",
                        "/decodejwt",
                        "/logout",
                        "/donations/**",
                        "/swagger-ui/**",
                        "/auth/**" // Public endpoint for Firebase token verification/login
                ).permitAll()
                .anyRequest().authenticated() // All other requests require authentication
            )
            // Configure OAuth2 Resource Server to validate Firebase ID Tokens
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter()) // Custom converter for roles
                )
            )
            // If you still want to use your custom JwtAuthenticationFilter for internal JWTs, keep it:
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Custom JWT converter to map Firebase roles/claims to Spring Security authorities
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Customize authority prefix if needed, default is "SCOPE_"
        // grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    // CORS Configuration (Keep this as is)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(corsAllowedOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}