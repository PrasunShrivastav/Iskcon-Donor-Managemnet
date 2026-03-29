package com.iskcongev.GEV_Donation.controller;

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

import com.iskcongev.GEV_Donation.model.CauseCategory;
import com.iskcongev.GEV_Donation.repository.CauseCategoryRepo;
import com.iskcongev.GEV_Donation.service.CauseCategoryService;

@RestController
@RequestMapping("/cause-categories")
public class CauseCategoryController {

    @Autowired
    private final CauseCategoryService causeCategoryService;

    private final CauseCategoryRepo causeCategoryRepo;


    public CauseCategoryController(CauseCategoryService causeCategoryService, CauseCategoryRepo causeCategoryRepo) {
        this.causeCategoryService = causeCategoryService;
        this.causeCategoryRepo = causeCategoryRepo;
    }

    @GetMapping("/")
    public ResponseEntity<List<CauseCategory>> getAllCategories() {
        List<CauseCategory> categories = causeCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CauseCategory> getCategoryById(@PathVariable Long id) {
        return causeCategoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/subcategory/{id}")
    public ResponseEntity<List<CauseCategory>> getSubCategory(@PathVariable String id) {
        List<CauseCategory> subCategories = causeCategoryService.getSubCategory(id);
        return ResponseEntity.ok(subCategories);
    }

    @GetMapping("/onlyCategory")
    public ResponseEntity<List<CauseCategory>> getOnlyCategory() {
        List<CauseCategory> onlyCategories = causeCategoryService.getCategoriesOnly("");
        return ResponseEntity.ok(onlyCategories);
    }

    @GetMapping("/allSubCategories")
    public ResponseEntity<List<CauseCategory>> getAllSubCategories() {
        List<CauseCategory> allSubCategories = causeCategoryService.getAllSubCategories();
        return ResponseEntity.ok(allSubCategories);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadCauseCategory(
            @RequestParam String name,
            @RequestParam(required = false) String parent_id,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) MultipartFile image_sm
            ) {
        try {
            CauseCategory saved = causeCategoryService.saveCategory(name, parent_id, description, image, image_sm);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CauseCategory> updateCategory(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String parent_id,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) MultipartFile image_sm
           ) throws Exception {
        return ResponseEntity.ok(causeCategoryService.updateCategory(id, name, parent_id, description, image, image_sm));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteCategory(@PathVariable Long id) {
        boolean deleted = causeCategoryService.softDeleteCategory(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (causeCategoryRepo.existsById(id)) {
            causeCategoryRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
