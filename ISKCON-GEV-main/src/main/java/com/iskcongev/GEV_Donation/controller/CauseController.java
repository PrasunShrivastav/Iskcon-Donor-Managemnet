package com.iskcongev.GEV_Donation.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iskcongev.GEV_Donation.model.Cause;
import com.iskcongev.GEV_Donation.repository.CauseRepo;
import com.iskcongev.GEV_Donation.service.CauseService;

@RestController
@RequestMapping("/causes")
public class CauseController {
    @Autowired
    private CauseService causeService;

    private final CauseRepo causeRepo;

    public CauseController(CauseRepo causeRepo) {
        this.causeRepo = causeRepo;
    }

    @PostMapping("/upload/{category_id}")
    public ResponseEntity<?> uploadCause(
        @PathVariable Long category_id,
        @RequestParam String display_name,
        @RequestParam Double default_amount,
        @RequestParam(name="image", required = false) MultipartFile image,
        @RequestParam(name="image_sm", required = false) MultipartFile image_sm
    ) {
        try {
            Cause saved = causeService.createCause(category_id, display_name, default_amount, image, image_sm);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Cause>> getCausesByCategory(
        @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(causeService.getCausesByCategory(categoryId));
    }

    @GetMapping("/")
     public ResponseEntity<List<Cause>> getAllCauses() {
        List<Cause> causes = causeService.getAllCauses();
        return ResponseEntity.ok(causes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cause> getCause(@PathVariable Long id) {
        return causeService.getCause(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cause> updateCause(
        @PathVariable Long id,
        @RequestParam(required = false) String display_name,
        @RequestParam(required = false) Double default_amount,
        @RequestParam(required = false) MultipartFile image,
        @RequestParam(required = false) MultipartFile image_sm
    ) throws Exception {
        return ResponseEntity.ok(causeService.updateCause(id, display_name, default_amount, image, image_sm));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteCategory(@PathVariable Long id) {
        boolean deleted = causeService.softDeleteCause(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (causeRepo.existsById(id)) {
            causeRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

