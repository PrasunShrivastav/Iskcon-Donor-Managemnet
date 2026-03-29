package com.iskcongev.GEV_Donation.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
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

import com.iskcongev.GEV_Donation.dto.DonorFilterRequest;
import com.iskcongev.GEV_Donation.model.Donor;
import com.iskcongev.GEV_Donation.service.DonorService;

@RestController
@RequestMapping("/donor")
public class DonorController {

    private final DonorService donorService;
    

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDonor(
        @RequestParam(required = true) Long prefix,
        @RequestParam(required = true) String first_name,
        @RequestParam(required = true) String last_name,
        @RequestParam(required = false) String email,
        @RequestParam(required = true) String contact_no,
        @RequestParam(required = false) String address_line_1,
        @RequestParam(required = false) String address_line_2,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) String pincode,
        @RequestParam(required = false) String pan_number

    ) {
        try {
            Donor saved = donorService.createDonor(prefix, first_name, last_name, email, contact_no, address_line_1, address_line_2, city, state, pincode, pan_number);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Cannot create new donor: " + e.getMessage());
        }
    }

  @PostMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterDonors(@RequestBody DonorFilterRequest request) {
        Page<Donor> page = donorService.filterDonors(request);

        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("totalElements", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        response.put("page", page.getNumber());
        response.put("size", page.getSize());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/")
    public ResponseEntity<List<Donor>> getAllDonors() {
        return ResponseEntity.ok(donorService.getAllDonors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donor> getDonorById(@PathVariable Long id) {
        return donorService.getDonorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Donor> updateDonor(@PathVariable Long id, @RequestBody Donor donor) {
        System.out.println("body: "+donor);
        return donorService.updateDonor(id, donor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteDonor(@PathVariable Long id) {
        boolean deleted = donorService.softDeleteDonor(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
