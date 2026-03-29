package com.iskcongev.GEV_Donation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.iskcongev.GEV_Donation.model.Donation;

public interface DonationRepo extends JpaRepository<Donation, Long>, JpaSpecificationExecutor<Donation> {
    List<Donation> findByDeleted(Boolean deleted);
    Page<Donation> findByDonorIdAndDeletedFalse(Long donorId, Pageable pageable);
    Optional<Donation> findByIdAndDeletedFalse(Long id);
}
