package com.iskcongev.GEV_Donation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iskcongev.GEV_Donation.model.Options;
import com.iskcongev.GEV_Donation.model.OptionsGroup;

@Repository
public interface OptionsRepo extends JpaRepository<Options,Long>{
    List<Options> findByDeleted(Boolean deleted);
    Optional<Options> findByIdAndDeletedFalse(Long id);
    List<Options> findByGroupAndDeletedFalse(OptionsGroup group);
    Page<Options> findByNameContainingIgnoreCaseAndDeletedFalse(String name, Pageable pageable);
}
