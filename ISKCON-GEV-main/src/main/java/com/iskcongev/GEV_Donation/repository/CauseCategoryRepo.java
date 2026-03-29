package com.iskcongev.GEV_Donation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iskcongev.GEV_Donation.model.CauseCategory;

@Repository
public interface CauseCategoryRepo extends JpaRepository<CauseCategory, Long> {
    boolean existsByName(String name);
    List<CauseCategory> findByDeleted(Boolean deleted);
    @Query("SELECT c FROM CauseCategory c WHERE c.parentId IS NOT NULL AND c.parentId <> '' AND c.deleted = false")
    List<CauseCategory> findAllWithParentId();
    List<CauseCategory> findByParentIdAndDeletedFalseOrderByParentIdAscNameAsc(String parent_id);
    List<CauseCategory> findByParentIdAndDeletedFalseOrderByNameAsc(String parent_id);
    Optional<CauseCategory> findByIdAndDeletedFalse(Long id);

}