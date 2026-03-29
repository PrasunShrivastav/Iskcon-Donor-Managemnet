package com.iskcongev.GEV_Donation.service;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.iskcongev.GEV_Donation.model.Donor;
import com.iskcongev.GEV_Donation.repository.DonorRepo;
import com.iskcongev.GEV_Donation.security.CustomOAuth2User;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final DonorService donorService;
    private final DonorRepo donorRepo;


    public CustomOAuth2UserService(DonorService donorService, DonorRepo donorRepo) {
        this.donorService = donorService;
        this.donorRepo = donorRepo;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Extract user attributes from Google
        String email = oauth2User.getAttribute("email");
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");
        String name = oauth2User.getAttribute("name");

        System.out.println("creds: "+email +" "+ firstName +" "+ lastName + name);

        Optional<Donor> existingDonor = donorRepo.findByEmail(email);

        Donor donor;
        if (existingDonor.isPresent()) {
            donor = existingDonor.get();
            donor.setFirst_name(firstName != null ? firstName : name); // Prioritize given_name, fallback to full name
            donor.setLast_name(lastName);
            donorRepo.save(donor); // Save updated donor
        } else {
            // Register new user
            donor = new Donor();
            donor.setEmail(email);
            donor.setFirst_name(firstName != null ? firstName : name);
            donor.setLast_name(lastName);
            donorRepo.save(donor); // Save new donor
        }


        return new CustomOAuth2User(oauth2User, donor.getId()); // Pass Donor ID to CustomOAuth2User
    }
}