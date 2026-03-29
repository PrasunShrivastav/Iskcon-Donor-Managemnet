package com.iskcongev.GEV_Donation.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iskcongev.GEV_Donation.model.OptionsGroup;
import com.iskcongev.GEV_Donation.service.OptionsGroupService;

@RestController
@RequestMapping("/options-groups")
public class OptionsGroupController {

    @Autowired
    private final OptionsGroupService optionsGroupService;

    public OptionsGroupController(OptionsGroupService optionsGroupService) {
        this.optionsGroupService = optionsGroupService;
    }

    @GetMapping("/")
    public ResponseEntity<List<OptionsGroup>> getAllCategories() {
        List<OptionsGroup> categories = optionsGroupService.getAllOptionsGroup();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptionsGroup> getCategoryById(@PathVariable Long id) {
        return optionsGroupService.getOptionsGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<OptionsGroup> createGroup(@RequestBody OptionsGroup createOptions) {
        OptionsGroup created = optionsGroupService.createOptionsGroup(createOptions.getName());
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OptionsGroup> updateGroup(@PathVariable Long id, @RequestBody OptionsGroup updatedGroup) {
        Optional<OptionsGroup> updated = optionsGroupService.updateGroup(id, updatedGroup);
        return updated.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        boolean deleted = optionsGroupService.softDeleteGroup(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
