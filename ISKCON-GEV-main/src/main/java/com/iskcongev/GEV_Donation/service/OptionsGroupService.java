package com.iskcongev.GEV_Donation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.iskcongev.GEV_Donation.model.OptionsGroup;
import com.iskcongev.GEV_Donation.repository.OptionsGroupRepo;

@Service
public class OptionsGroupService {

    private final OptionsGroupRepo optionsGroupRepo;

    public OptionsGroupService(OptionsGroupRepo optionsGroupRepo) {
        this.optionsGroupRepo = optionsGroupRepo;
    }

     public List<OptionsGroup> getAllOptionsGroup(){
        return optionsGroupRepo.findByDeleted(false);
    }

    public Optional<OptionsGroup> getOptionsGroupById(Long id) {
        return optionsGroupRepo.findByIdAndDeletedFalse(id);
    }
    
    public OptionsGroup createOptionsGroup(String name){
        OptionsGroup newOptionsGroup = new OptionsGroup();
        newOptionsGroup.setName(name);
        return optionsGroupRepo.save(newOptionsGroup);
    }

    public Optional<OptionsGroup> updateGroup(Long id, OptionsGroup updatedGroup) {
        return optionsGroupRepo.findById(id).map(existing -> {
            if(updatedGroup.getName() != null){
                existing.setName(updatedGroup.getName());
                existing.setUpdated_at(LocalDateTime.now());
            }
           
            return optionsGroupRepo.save(existing);
        });
    }

    public boolean softDeleteGroup(Long id) {
        return optionsGroupRepo.findById(id).map(group -> {
            group.setDeleted(true);
            group.setDeleted_at(LocalDateTime.now());
            optionsGroupRepo.save(group);
            return true;
        }).orElse(false);
    }

}
