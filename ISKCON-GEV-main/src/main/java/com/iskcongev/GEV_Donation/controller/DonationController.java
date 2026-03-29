package com.iskcongev.GEV_Donation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iskcongev.GEV_Donation.dto.DonationFilterRequest;
import com.iskcongev.GEV_Donation.model.Donation;
import com.iskcongev.GEV_Donation.model.Donor;
import com.iskcongev.GEV_Donation.service.DonationService;
import com.iskcongev.GEV_Donation.service.DonorService;
import com.iskcongev.GEV_Donation.service.JWTService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/donations")
public class DonationController {

    @Autowired
    private final DonationService donationService;
    private final DonorService donorService;
    private final JWTService jwtService;

    public DonationController(DonationService donationService, DonorService donorService, JWTService jwtService) {
        this.donationService = donationService;
        this.donorService = donorService;
        this.jwtService = jwtService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Donation>> getAll() {
        return ResponseEntity.ok(donationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donation> getById(@PathVariable Long id) {
        return donationService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("donor/{id}")
    public ResponseEntity<Page<Donation>> getPaginatedDonationsByDonorId(
            @PathVariable Long id,
            Pageable pageable) { // Spring automatically resolves page, size, sort from request params
        Page<Donation> donationsPage = donationService.getPaginatedDonationsByDonorId(id, pageable);
        return ResponseEntity.ok(donationsPage);
    }

    @PostMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterDonations(@RequestBody DonationFilterRequest request) {
        System.out.println("request :"+request);

        Page<Donation> page = donationService.filterDonations(request);
        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("totalElements", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        response.put("page", page.getNumber());
        response.put("size", page.getSize());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody List<Donation> donation, HttpServletRequest request) {
    
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
    
        List<Donation> addDonation = donationService.createDonation(donor, donation);
        System.out.println("Saved donations: " + addDonation);
    
        return ResponseEntity.ok(addDonation);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Donation> update(@PathVariable Long id, @RequestBody Donation updatedDonation) {
        return donationService.update(id, updatedDonation)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam(defaultValue = "system") String deletedBy) {
        if (donationService.softDelete(id, deletedBy)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
