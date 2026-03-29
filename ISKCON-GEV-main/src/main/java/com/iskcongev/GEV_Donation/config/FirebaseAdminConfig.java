package com.iskcongev.GEV_Donation.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseAdminConfig {

    @Value("classpath:iskcon-ciam-firebase-adminsdk-ymugz-de8baf122d.json") // Path to your downloaded JSON key
    private Resource serviceAccount;

    @PostConstruct
    public void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) { // Ensure it's initialized only once
            InputStream serviceAccountStream = serviceAccount.getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase Admin SDK initialized successfully.");
        }
    }
}