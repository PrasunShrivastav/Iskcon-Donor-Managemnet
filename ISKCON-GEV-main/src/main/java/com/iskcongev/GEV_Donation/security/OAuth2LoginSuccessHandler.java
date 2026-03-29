package com.iskcongev.GEV_Donation.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.iskcongev.GEV_Donation.service.JWTService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTService jwtService;

    // Inject your frontend redirect URL from properties
    @Value("${app.oauth2.redirect-frontend-url}")
    private String frontendRedirectUrl;

    public OAuth2LoginSuccessHandler(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // The 'authentication.getPrincipal()' here will be the CustomOAuth2User
        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();

        // Get your internal donor ID
        Long donorId = oauth2User.getDonorId();
        String email = oauth2User.getEmail(); // Use email as subject for JWT

        // Generate your application's JWT
        String jwtToken = jwtService.generateToken(email); // Assuming generateToken takes email and donorId

        // Set the JWT as an HttpOnly cookie
        Cookie jwtCookie = new Cookie("jwt", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/"); // Make it available across the entire application
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days (adjust as needed)
        jwtCookie.setSecure(true); // Only send over HTTPS in production!
        response.addCookie(jwtCookie);

        // Redirect to your frontend application's dashboard or a specific page
        // The frontend will then use this cookie for subsequent authenticated requests.
        response.sendRedirect(frontendRedirectUrl);
    }
}