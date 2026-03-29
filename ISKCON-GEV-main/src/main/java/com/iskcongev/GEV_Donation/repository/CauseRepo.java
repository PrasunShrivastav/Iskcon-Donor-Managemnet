package com.iskcongev.GEV_Donation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iskcongev.GEV_Donation.model.Cause;

@Repository
public interface CauseRepo extends JpaRepository<Cause, Long> {
    List<Cause> findByCategory_Id(Long categoryId);
    List<Cause> findByDeleted(Boolean deleted);
    Optional<Cause> findByIdAndDeletedFalse(Long id);
}

