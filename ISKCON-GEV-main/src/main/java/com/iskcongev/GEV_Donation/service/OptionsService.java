package com.iskcongev.GEV_Donation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.iskcongev.GEV_Donation.model.Options;
import com.iskcongev.GEV_Donation.model.OptionsGroup;
import com.iskcongev.GEV_Donation.repository.OptionsGroupRepo;
import com.iskcongev.GEV_Donation.repository.OptionsRepo;

@Service
public class OptionsService {
    
    private final OptionsRepo optionsRepo;
    private final OptionsGroupRepo optionsGroupRepo;


    public OptionsService(OptionsRepo optionsRepo, OptionsGroupRepo optionsGroupRepo) {
        this.optionsRepo = optionsRepo;
        this.optionsGroupRepo = optionsGroupRepo;
    }

    public Options createOption(Long options_group_id, String name) {
        Options saveOptions = new Options();
        OptionsGroup optionsGroupEntity = optionsGroupRepo.findByIdAndDeletedFalse(options_group_id)
        .orElseThrow(() -> new RuntimeException("Options Group not found"));
        saveOptions.setGroup(optionsGroupEntity);
        saveOptions.setName(name);
        return optionsRepo.save(saveOptions);
    }

    public List<Options> getAllOptions() {
        return optionsRepo.findAll();
    }

    public Optional<Options> getOptionById(Long id) {
        return optionsRepo.findById(id).filter(o -> !Boolean.TRUE.equals(o.getDeleted()));
    }

    public List<Options> getOptionsByGroup(OptionsGroup group) {
        return optionsRepo.findByGroupAndDeletedFalse(group);
    }

    public Page<Options> searchOptions(String name, Pageable pageable) {
        return optionsRepo.findByNameContainingIgnoreCaseAndDeletedFalse(name, pageable);
    }

    public Optional<Options> updateOption(Long id, Options updatedOption) {
        return optionsRepo.findById(id).map(existing -> {
            if (updatedOption.getName() != null) {
                existing.setName(updatedOption.getName());
            }
            if (updatedOption.getGroup() != null) {
                existing.setGroup(updatedOption.getGroup());
            }
            existing.setUpdated_at(LocalDateTime.now());
            return optionsRepo.save(existing);
        });
    }

    public boolean softDeleteOption(Long id) {
        return optionsRepo.findById(id).map(opt -> {
            opt.setDeleted(true);
            opt.setDeleted_at(LocalDateTime.now());
            optionsRepo.save(opt);
            return true;
        }).orElse(false);
    }

}
