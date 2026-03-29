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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iskcongev.GEV_Donation.model.Options;
import com.iskcongev.GEV_Donation.service.OptionsService;

@RestController
@RequestMapping("/options")
public class OptionsController {

    @Autowired
    private final OptionsService optionsService;

    public OptionsController(OptionsService optionsService) {
        this.optionsService = optionsService;
    }

    @PostMapping("/{options_group_id}")
    public ResponseEntity<Options> createOption(@PathVariable Long options_group_id, @RequestParam String  name) {
            Options saved = optionsService.createOption(options_group_id, name);
            return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Options> updateOption(@PathVariable Long id, @RequestBody Options updatedOption) {
        Optional<Options> updated = optionsService.updateOption(id, updatedOption);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        boolean deleted = optionsService.softDeleteOption(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<Options>> getAllOptions() {
        return ResponseEntity.ok(optionsService.getAllOptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Options> getOptionById(@PathVariable Long id) {
        Optional<Options> option = optionsService.getOptionById(id);
        return option.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
