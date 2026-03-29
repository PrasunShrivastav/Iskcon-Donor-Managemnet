package com.iskcongev.GEV_Donation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.iskcongev.GEV_Donation.dto.DonorFilterRequest;
import com.iskcongev.GEV_Donation.model.Donor;
import com.iskcongev.GEV_Donation.model.Options;
import com.iskcongev.GEV_Donation.repository.DonorRepo;
import com.iskcongev.GEV_Donation.repository.OptionsRepo;

import jakarta.persistence.criteria.Predicate;

@Service
public class DonorService {

    private final DonorRepo donorRepo;
    private final OptionsRepo optionsRepo;

    public DonorService(DonorRepo donorRepo, OptionsRepo optionsRepo) {
        this.donorRepo = donorRepo;
        this.optionsRepo = optionsRepo;
    }


    public Donor createDonor(Long prefix,String first_name,String last_name,String email,String contact_no,String address_line_1,String address_line_2,String city,String state,String pincode,String pan_number) {
        Donor newDonor = new Donor();
        Options verifyOptions = optionsRepo.findByIdAndDeletedFalse(prefix)
        .orElseThrow(() -> new RuntimeException("Prefix not found"));
        newDonor.setPrefix(verifyOptions);
        newDonor.setFirst_name(first_name);
        newDonor.setLast_name(last_name);
        newDonor.setEmail(email);
        newDonor.setContact_no(contact_no);
        newDonor.setAddress_line_1(address_line_1);
        newDonor.setAddress_line_2(address_line_2);
        newDonor.setCity(city);
        newDonor.setState(state);
        newDonor.setPincode(pincode);
        newDonor.setPan_number(pan_number);
        return donorRepo.save(newDonor);
    }

    public List<Donor> getAllDonors() {
        return donorRepo.findAll();
    }

    public Optional<Donor> getDonorById(Long id) {
        return donorRepo.findById(id);
    }
    
    public Optional<Donor> verifyDonor(String userId) {
        if(userId.contains("@")){
            return donorRepo.findByEmail(userId);
        }else{
            return donorRepo.findByContactNo(userId);
        }
    }

   public Page<Donor> filterDonors(DonorFilterRequest request) {
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

    return donorRepo.findAll((root, query, criteriaBuilder) -> {
        List<Predicate> predicates = new ArrayList<>();

        if (request.getGlobal() != null && !request.getGlobal().isBlank()) {
            String searchPattern = "%" + request.getGlobal().toLowerCase() + "%";
            predicates.add(criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("first_name")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("last_name")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("contactNo")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("address_line_1")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("address_line_2")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("state")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("pincode")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("pan_number")), searchPattern)
            ));
        }

        if (request.getCreatedFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                root.get("createdAt").as(LocalDate.class), request.getCreatedFrom()));
        }

        if (request.getCreatedTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(
                root.get("createdAt").as(LocalDate.class), request.getCreatedTo()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }, pageable);
}

    public Optional<Donor> verifyAdmin(String userId, String password) {
        return donorRepo.findByEmailAndContactNo(userId, password);
    }

    public Donor findOrCreateDonor(String email, String firstName, String lastName) {
        return donorRepo.findByEmail(email)
                .orElseGet(() -> {
                    Donor newDonor = new Donor();
                    newDonor.setEmail(email);
                    newDonor.setFirst_name(firstName != null ? firstName : "NA");
                    newDonor.setLast_name(lastName != null ? lastName : "NA");
                    return donorRepo.save(newDonor);
                });
    }

    public Optional<Donor> updateDonor(Long id, Donor updatedData) {
        System.out.println("body service: "+updatedData.getAddress_line_2());
        return donorRepo.findById(id).map(existing -> {

            if (updatedData.getFirst_name() != null) existing.setFirst_name(updatedData.getFirst_name());
            if (updatedData.getLast_name() != null) existing.setLast_name(updatedData.getLast_name());
            if (updatedData.getEmail() != null) existing.setEmail(updatedData.getEmail());
            if (updatedData.getContact_no() != null) existing.setContact_no(updatedData.getContact_no());
            if (updatedData.getAlt_contact_no() != null) existing.setAlt_contact_no(updatedData.getAlt_contact_no());
            if (updatedData.getPan_number() != null) existing.setPan_number(updatedData.getPan_number());
            if (updatedData.getAddress_line_1() != null) existing.setAddress_line_1(updatedData.getAddress_line_1());
            if (updatedData.getAddress_line_2() != null) existing.setAddress_line_2(updatedData.getAddress_line_2());
            if (updatedData.getCity() != null) existing.setCity(updatedData.getCity());
            if (updatedData.getState() != null) existing.setState(updatedData.getState());
            if (updatedData.getPincode() != null) existing.setPincode(updatedData.getPincode());
            if (updatedData.getFacilitator() != null) existing.setFacilitator(updatedData.getFacilitator());
            if (updatedData.getPrefix() != null) existing.setPrefix(updatedData.getPrefix());

            existing.setUpdated_at(LocalDateTime.now());

            return donorRepo.save(existing);
        });
    }

    public boolean softDeleteDonor(Long id) {
        return donorRepo.findById(id).map(donor -> {
            donor.setDeleted(true);
            donor.setDeleted_at(LocalDateTime.now());
            donorRepo.save(donor);
            return true;
        }).orElse(false);
    }
}
