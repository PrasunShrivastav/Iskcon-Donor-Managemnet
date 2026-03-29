package com.iskcongev.GEV_Donation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iskcongev.GEV_Donation.exception.DuplicateCategoryException;
import com.iskcongev.GEV_Donation.model.CauseCategory;
import com.iskcongev.GEV_Donation.repository.CauseCategoryRepo;

@Service
public class CauseCategoryService {

    private final CauseCategoryRepo causeCategoryRepo;
    private final FileStorageService fileStorageService;
    private final SmallFileStorageService smallFileStorageService;


    public CauseCategoryService(CauseCategoryRepo causeCategoryRepo,FileStorageService fileStorageService, SmallFileStorageService smallFileStorageService) {
        this.causeCategoryRepo = causeCategoryRepo;
        this.fileStorageService = fileStorageService;
        this.smallFileStorageService = smallFileStorageService;
    }

    public CauseCategory saveCategory(String name,String parent_id, String description, MultipartFile image, MultipartFile image_sm) throws Exception {
         if (causeCategoryRepo.existsByName(name)) {
            throw new DuplicateCategoryException("Cause category with name '" + name + "' already exists.");
        }
        CauseCategory category = new CauseCategory();

        System.out.println(description);

        if (name != null) {
            category.setName(name);
        }

        if (parent_id != null) {
            category.setParent_id(parent_id);
        }

        if (description != null) {
            category.setDescription(description);
        }

        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeFile(image);
            category.setImage_url(imagePath);
        }

        if (image_sm != null && !image_sm.isEmpty()) {
            String imagePathSM = smallFileStorageService.storeFile(image_sm);
            category.setImage_url_sm(imagePathSM);
        }

        return causeCategoryRepo.save(category);
    }

    public List<CauseCategory> getAllCategories(){
        return causeCategoryRepo.findAll(Sort.by("parentId").ascending()
        .and(Sort.by("name").ascending()).and(Sort.by("deleted").descending()));
    }

    public List<CauseCategory> getCategoriesOnly(String parent_id){
        return causeCategoryRepo.findByParentIdAndDeletedFalseOrderByNameAsc(parent_id);
    }

    public Optional<CauseCategory> getCategoryById(Long id) {
        return causeCategoryRepo.findByIdAndDeletedFalse(id);
    }

    public List<CauseCategory> getSubCategory(String parent_id) {
        return causeCategoryRepo.findByParentIdAndDeletedFalseOrderByParentIdAscNameAsc(parent_id);
    }
    
    public List<CauseCategory> getAllSubCategories() {
        return causeCategoryRepo.findAllWithParentId();
    }

    public CauseCategory updateCategory(Long id, String name, String parent_id, String description, MultipartFile image, MultipartFile image_sm)  throws Exception {
        CauseCategory category = causeCategoryRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        if (name!= null) {
            category.setName(name);
        }
        if (description != null) {
            category.setDescription(description);
        }
        if (parent_id != null) {
            category.setParent_id(parent_id);
        }
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeFile(image);
            category.setImage_url(imagePath);
        }
        if (image_sm != null && !image_sm.isEmpty()) {
            String imagePathSM = smallFileStorageService.storeFile(image_sm);
            category.setImage_url_sm(imagePathSM);
        }
        category.setUpdated_at(LocalDateTime.now());
        return causeCategoryRepo.save(category);

    }

    public boolean softDeleteCategory(Long id) {
        return causeCategoryRepo.findById(id).map(category -> {
            category.setDeleted(true);
            category.setDeleted_at(LocalDateTime.now());
            // Optionally set deleted_by_user_id
            causeCategoryRepo.save(category);
            return true;
        }).orElse(false);
    }
}
