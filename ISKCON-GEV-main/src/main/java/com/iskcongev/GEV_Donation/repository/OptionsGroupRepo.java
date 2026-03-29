package com.iskcongev.GEV_Donation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iskcongev.GEV_Donation.model.OptionsGroup;

@Repository
public interface OptionsGroupRepo extends JpaRepository<OptionsGroup, Long> {
    List<OptionsGroup> findByDeleted(Boolean deleted);
    Optional<OptionsGroup> findByIdAndDeletedFalse(Long id);

}

