package com.iskcongev.GEV_Donation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.iskcongev.GEV_Donation.model.Donor;

@Repository
public interface DonorRepo extends JpaRepository<Donor, Long>, JpaSpecificationExecutor<Donor> {
    List<Donor> findByDeleted(Boolean deleted);
    Optional<Donor> findByEmail(String email);
    Optional<Donor> findByContactNo(String contactNo);
    Optional<Donor> findByEmailAndContactNo(String email, String contactNo);
    Optional<Donor> findByIdAndDeletedFalse(Long id);
}
