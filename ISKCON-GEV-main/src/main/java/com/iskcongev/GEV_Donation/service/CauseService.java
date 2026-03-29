package com.iskcongev.GEV_Donation.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iskcongev.GEV_Donation.model.Cause;
import com.iskcongev.GEV_Donation.model.CauseCategory;
import com.iskcongev.GEV_Donation.repository.CauseCategoryRepo;
import com.iskcongev.GEV_Donation.repository.CauseRepo;

@Service
public class CauseService {
    
    private final CauseRepo causeRepo;
    private final CauseCategoryRepo categoryRepo;
    private final FileStorageService fileStorageService;
    private final SmallFileStorageService smallFileStorageService;

    public CauseService(CauseCategoryRepo categoryRepo, CauseRepo causeRepo, FileStorageService fileStorageService, SmallFileStorageService smallFileStorageService) {
        this.categoryRepo = categoryRepo;
        this.causeRepo = causeRepo;
        this.fileStorageService = fileStorageService;
        this.smallFileStorageService = smallFileStorageService;
    }

    public Cause createCause(Long category, String display_name, Double default_amount, MultipartFile image, MultipartFile image_sm) throws IOException {
        Cause cause = new Cause();
    
        if (category != null) {
            CauseCategory categoryEntity = categoryRepo.findById(category)
                .orElseThrow(() -> new RuntimeException("Category not found"));
            cause.setCategory(categoryEntity);
        }
    
        if (display_name != null && !display_name.trim().isEmpty()) {
            cause.setDisplay_name(display_name.trim());
        }
    
        if (default_amount != null) {
            cause.setDefault_amount(default_amount);
        }
    
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeFile(image);
            cause.setImage_url(imagePath);
        }
    
        if (image_sm != null && !image_sm.isEmpty()) {
            String imagePathSM = smallFileStorageService.storeFile(image_sm);
            cause.setImage_url_sm(imagePathSM);
        }
    
        return causeRepo.save(cause);
    }
    

    public List<Cause> getCausesByCategory(Long category_id) {
        return causeRepo.findByCategory_Id(category_id);
    }

    public Optional<Cause> getCause(Long id) {
        return causeRepo.findByIdAndDeletedFalse(id);
    }

    public List<Cause> getAllCauses(){
        return causeRepo.findAll();
    }

    public Cause updateCause(Long id,  String display_name, Double default_amount, MultipartFile image, MultipartFile image_sm) throws Exception {
        Cause cause = causeRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Cause not found"));
        if(display_name != null){
            cause.setDisplay_name(display_name);
        }
        if(default_amount != null){
            cause.setDefault_amount(default_amount);
        }
        if(image != null && !image.isEmpty()){
            String imagePath = fileStorageService.storeFile(image);
            cause.setImage_url(imagePath);

        }
        if(image_sm != null && !image_sm.isEmpty()){
            String imagePathSM = smallFileStorageService.storeFile(image_sm);
            cause.setImage_url_sm(imagePathSM);
        }
        cause.setUpdated_at(LocalDateTime.now());

        return causeRepo.save(cause);
    }

     public boolean softDeleteCause(Long id) {
        return causeRepo.findById(id).map(cause -> {
            cause.setDeleted(true);
            cause.setDeleted_at(LocalDateTime.now());
            // Optionally set deleted_by_user_id
            causeRepo.save(cause);
            return true;
        }).orElse(false);
    }
}
