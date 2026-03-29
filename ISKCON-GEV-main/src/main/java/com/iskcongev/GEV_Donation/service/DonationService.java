package com.iskcongev.GEV_Donation.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.iskcongev.GEV_Donation.dto.DonationFilterRequest;
import com.iskcongev.GEV_Donation.model.Cause;
import com.iskcongev.GEV_Donation.model.Donation;
import com.iskcongev.GEV_Donation.model.Donor;
import com.iskcongev.GEV_Donation.repository.CauseRepo;
import com.iskcongev.GEV_Donation.repository.DonationRepo;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
public class DonationService {
    private final DonationRepo donationRepo;
    private final CauseRepo causeRepo;

    public DonationService(DonationRepo donationRepo, CauseRepo causeRepo) {
        this.donationRepo = donationRepo;
        this.causeRepo = causeRepo;
    }

    public List<Donation> getAll() {
        return donationRepo.findByDeleted(false);
    }

    public Optional<Donation> getById(Long id) {
        return donationRepo.findByIdAndDeletedFalse(id);
    }

    public Page<Donation> getPaginatedDonationsByDonorId(Long donorId, Pageable pageable) {
        return donationRepo.findByDonorIdAndDeletedFalse(donorId, pageable);
    }

    @Transactional
    public List<Donation> createDonation(Donor donor, List<Donation> donations) {
        List<Donation> savedDonations = new ArrayList<>();
        for (Donation req : donations) {
            System.out.println("cause id : " + req.getId());
            System.out.println("amount : " + req.getAmount());
            System.out.println("qty : " + req.getQuantity());
            System.out.println("donor  : " + donor);

            Cause cause = causeRepo.findByIdAndDeletedFalse(req.getId())
            .orElseThrow(() -> new RuntimeException("Cause not found"));
            Donation donation = new Donation();
            donation.setDonor(donor);
            donation.setCauseId(cause);
            donation.setAmount(req.getAmount());
            donation.setQuantity(req.getQuantity());
            System.out.println("reached end of for loop");
            savedDonations.add(donationRepo.save(donation));
            System.out.println("reached end of for loop with add donation"+ savedDonations);

        }
        return savedDonations;
    }

    public Page<Donation> filterDonations(DonationFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "createdAt"));

        Specification<Donation> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getSearch() != null && !request.getSearch().isBlank()) {
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("receipt_id")), "%" + request.getSearch().toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("city")), "%" + request.getSearch().toLowerCase() + "%")
                ));
            }

            if (request.getDonorName() != null && !request.getDonorName().isBlank()) {
                Join<Donation, Donor> donorJoin = root.join("donor", JoinType.LEFT);
                predicates.add(cb.or(
                    cb.like(cb.lower(donorJoin.get("first_name")), "%" + request.getDonorName().toLowerCase() + "%"),
                    cb.like(cb.lower(donorJoin.get("last_name")), "%" + request.getDonorName().toLowerCase() + "%")
                ));
            }

            if (request.getContactNo() != null && !request.getContactNo().isBlank()) {
                Join<Donation, Donor> donorJoin = root.join("donor", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(donorJoin.get("contactNo")), "%" + request.getContactNo().toLowerCase() + "%"));
            }

            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                Join<Donation, Donor> donorJoin = root.join("donor", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(donorJoin.get("email")), "%" + request.getEmail().toLowerCase() + "%"));
            }

            if (request.getCauseName() != null && !request.getCauseName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.join("cause").get("display_name")), "%" + request.getCauseName().toLowerCase() + "%"));
            }

            if (request.getCategory() != null) {
                // CauseCategory has a String parentId field, not a JPA relationship.
                // Filter by the category's own ID instead of joining a non-existent 'parent'.
                predicates.add(cb.equal(root.join("cause").join("category").get("id"), request.getCategory()));
            }

            if (request.getSubCategory() != null && !request.getSubCategory().isBlank()) {
                predicates.add(cb.like(cb.lower(root.join("cause").join("category").get("name")), "%" + request.getSubCategory().toLowerCase() + "%"));
            }

            if (request.getCreatedFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getCreatedFrom().atStartOfDay()));
            }

            if (request.getCreatedTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getCreatedTo().atTime(LocalTime.MAX)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return donationRepo.findAll(spec, pageable);
    }

    public Optional<Donation> update(Long id, Donation updatedDonation) {
        return donationRepo.findById(id).map(existing -> {
            if (updatedDonation.getAmount() != null) existing.setAmount(updatedDonation.getAmount());
            if (updatedDonation.getPurpose() != null) existing.setPurpose(updatedDonation.getPurpose());
            if (updatedDonation.getPaymentMode() != null) existing.setPaymentMode(updatedDonation.getPaymentMode());
            if (updatedDonation.getCauseId() != null) existing.setCauseId(updatedDonation.getCauseId());
            if (updatedDonation.getDonor() != null) existing.setDonor(updatedDonation.getDonor());
            if (updatedDonation.getReceipt_id() != null) existing.setReceipt_id(updatedDonation.getReceipt_id());
            if (updatedDonation.getAddress_line_1() != null) existing.setAddress_line_1(updatedDonation.getAddress_line_1());
            if (updatedDonation.getAddress_line_2() != null) existing.setAddress_line_2(updatedDonation.getAddress_line_2());
            if (updatedDonation.getCity() != null) existing.setCity(updatedDonation.getCity());
            if (updatedDonation.getState() != null) existing.setState(updatedDonation.getState());
            if (updatedDonation.getPincode() != null) existing.setPincode(updatedDonation.getPincode());

            existing.setUpdated_at(LocalDateTime.now());
            return donationRepo.save(existing);
        });
    }

    public boolean softDelete(Long id, String deletedBy) {
        return donationRepo.findById(id).map(donation -> {
            donation.setDeleted(true);
            donation.setDeleted_at(LocalDateTime.now());
            donation.setDeleted_by_user_id(deletedBy);
            donationRepo.save(donation);
            return true;
        }).orElse(false);
    }
}
