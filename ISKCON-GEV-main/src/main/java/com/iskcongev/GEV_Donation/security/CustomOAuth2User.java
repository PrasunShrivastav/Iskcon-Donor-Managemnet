package com.iskcongev.GEV_Donation.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private OAuth2User oauth2User;
    private Long donorId; // Your internal donor ID

    public CustomOAuth2User(OAuth2User oauth2User, Long donorId) {
        this.oauth2User = oauth2User;
        this.donorId = donorId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        // This typically returns the 'sub' claim (Google ID) or 'name'
        return oauth2User.getName();
    }

    public Long getDonorId() {
        return donorId;
    }

    // You can add other getters for email, firstName, etc., if needed
    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

    public String getFirstName() {
        return oauth2User.getAttribute("given_name");
    }

    public String getLastName() {
        return oauth2User.getAttribute("family_name");
    }
}