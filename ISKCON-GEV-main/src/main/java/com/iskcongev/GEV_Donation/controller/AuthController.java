package com.iskcongev.GEV_Donation.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.iskcongev.GEV_Donation.model.Donor;
import com.iskcongev.GEV_Donation.repository.DonorRepo;
import com.iskcongev.GEV_Donation.service.DonorService;
import com.iskcongev.GEV_Donation.service.JWTService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    private final JWTService jwtService;
    private final DonorService donorService;
    private final DonorRepo donorRepo;

    public AuthController(JWTService jwtService, DonorService donorService, DonorRepo donorRepo) {
        this.jwtService = jwtService;
        this.donorService = donorService;
        this.donorRepo = donorRepo;
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<?> oauthSuccess(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        String firstName = principal.getAttribute("given_name");
        String lastName = principal.getAttribute("family_name");

        if (firstName == null && lastName == null) {
            String fullName = principal.getAttribute("name");
            if (fullName != null) {
                String[] parts = fullName.split(" ", 2);
                firstName = parts[0];
                if (parts.length > 1) lastName = parts[1];
            }
        }

        Donor donor = donorService.findOrCreateDonor(email, firstName, lastName);
        String token = jwtService.generateToken(email);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
            .httpOnly(true)
            .secure(false) // IMPORTANT: set true in production (requires HTTPS)
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .sameSite("Lax")
            .build();

        return ResponseEntity.ok()
            .header("Set-Cookie", jwtCookie.toString())
            .body(Map.of(
                "message", "OAuth login successful",
                "email", donor.getEmail(),
                "jwt", jwtCookie.toString()

            ));
    }

    @GetMapping("/verifyDonor")
    public ResponseEntity<?> verifyUserId(@RequestParam String userId, HttpServletRequest request) {
        Optional<Donor> donor = donorService.verifyDonor(userId);
        System.out.println("donor"+donor);
        if (donor.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }else{
            String token = jwtService.generateToken(userId);
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
            .httpOnly(true)
            .secure(false) // IMPORTANT: set true in production (requires HTTPS)
            .path("/")
            .maxAge(7 * 24 * 60 * 60) // 7 days
            .sameSite("Lax")
            .build();

            return ResponseEntity.ok()
            .header("Set-Cookie", jwtCookie.toString())
            .body(Map.of(
                "message", "Login successful"
            ));
        }
    }

    @GetMapping("/verifyAdmin")
    public ResponseEntity<?> verifyAdmin(@RequestParam String userId, @RequestParam String password, HttpServletRequest request) {
        Optional<Donor> donor = donorService.verifyAdmin(userId, password);
        System.out.println("donor"+donor);
        if (donor.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }else{
            String token = jwtService.generateToken(userId);
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
            .httpOnly(true)
            .secure(false) // IMPORTANT: set true in production (requires HTTPS)
            .path("/")
            .maxAge(7 * 24 * 60 * 60) // 7 days
            .sameSite("Lax")
            .build();

            return ResponseEntity.ok()
            .header("Set-Cookie", jwtCookie.toString())
            .body(Map.of(
                "message", "Login successful"
            ));
        }
    }

    @GetMapping("/decodejwt")
    public ResponseEntity<?> decodeJWT(HttpServletRequest request) {

     String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
    
        if (token == null) {
            return ResponseEntity.status(401).body("No JWT token found in cookies.");
        }
    
        Claims claims;
        try {
            claims = jwtService.getClaims(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid JWT token: " + e.getMessage());
        }
    
        String donorId = claims.get("sub", String.class);
        if (donorId == null) {
            return ResponseEntity.status(400).body("JWT does not contain 'sub' claim.");
        }
    
        Donor donor = donorService.verifyDonor(donorId)
            .orElseThrow(() -> {
                System.out.println("ERROR: Donor not found for id: " + donorId);
                return new RuntimeException("Donor not found");
            });    

        System.out.println("donor from jwt: "+ donor);

        return ResponseEntity.ok(donor);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .secure(false) // true in production (HTTPS)
            .path("/")
            .maxAge(0) // expires immediately
            .sameSite("Lax")
            .build();

            System.out.println("deleteCookie"+deleteCookie);

        return ResponseEntity.ok()
            .header("Set-Cookie", deleteCookie.toString())
            .body(Map.of(
                "message", "Logged out successfully",
                "jwt", deleteCookie.toString()
                ));
    }

    @PostMapping("/verify-id-token")
    public ResponseEntity<?> verifyIdToken(@RequestBody Map<String, String> requestBody) {
        String idToken = requestBody.get("idToken");

        if (idToken == null || idToken.isEmpty()) {
            return ResponseEntity.badRequest().body("ID Token is required.");
        }

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", idToken)
            .httpOnly(true)
            .secure(false) // IMPORTANT: set true in production (requires HTTPS)
            .path("/")
            .maxAge(7 * 24 * 60 * 60) // 7 days
            .sameSite("Lax")
            .build();

        try {
            // Verify the Firebase ID Token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String displayName = decodedToken.getName();
            String photoUrl = decodedToken.getPicture();

            // Check if user exists in GIP (Firebase Authentication)
            UserRecord userRecord;
            try {
                userRecord = FirebaseAuth.getInstance().getUser(uid);
            } catch (FirebaseAuthException e) {
                // User does not exist in GIP, this case usually means the token is invalid or user was deleted
                // If you want to *create* the user in GIP from here (less common for ID token verification,
                // more for migration or specific sync scenarios), you'd do it here.
                // For typical Firebase Auth flow, the frontend handles GIP user creation on first sign-up.
                System.err.println("Firebase user not found or token invalid: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired ID Token.");
            }

            // Now, synchronize with your local Donor database if needed.
            // This is where you implement "see if user exists in it (your local DB) else create one"
            // based on the Firebase user's information.
            // Donor donor = donorRepo.findByEmail(email).orElseGet(() -> {
            //     Donor newDonor = new Donor();
            //     newDonor.setEmail(email);
            //     // Parse display name into first/last name if not explicitly provided
            //     if (displayName != null) {
            //         String[] nameParts = displayName.split(" ", 2);
            //         newDonor.setFirst_name(nameParts[0]);
            //         if (nameParts.length > 1) {
            //             newDonor.setLast_name(nameParts[1]);
            //         }
            //     }
            //     newDonor.setCreated_at(LocalDateTime.now());
            //     newDonor.setDeleted(false);
            //     // Handle contactNo default if it's NOT NULL in your Donor model/DB
            //     // newDonor.setContactNo("N/A");
            //     return donorRepo.save(newDonor);
            // });

            // Return success response, perhaps with your internal Donor details
            return ResponseEntity.ok()
            .header("Set-Cookie", jwtCookie.toString())
            .body(
                Map.of(
                "message", "Firebase ID Token verified and user synchronized.",
                "uid", uid,
                "email", email,
                "displayName", displayName
                // "localDonorId", donor.getId() // Your internal DB ID
            ));

        } catch (FirebaseAuthException e) {
            System.err.println("Firebase ID Token verification failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired ID Token.");
        } catch (Exception e) {
            System.err.println("Unexpected error during token verification: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


}
