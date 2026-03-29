package com.iskcongev.GEV_Donation.dto;

import java.time.LocalDate;

public class DonationFilterRequest {
    private String search;
    private String causeName;
    private String subCategory;
    private Long categoryName;
    private String donorName;
    private String contactNo;
    private String email;
    private LocalDate createdFrom;
    private LocalDate createdTo;
    private int page = 0;
    private int size = 10;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getCauseName() {
        return causeName;
    }

    public void setCauseName(String causeName) {
        this.causeName = causeName;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public Long getCategory() {
        return categoryName;
    }

    public void setCategory(Long categoryName) {
        this.categoryName = categoryName;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public LocalDate getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(LocalDate createdFrom) {
        this.createdFrom = createdFrom;
    }

    public LocalDate getCreatedTo() {
        return createdTo;
    }

    public void setCreatedTo(LocalDate createdTo) {
        this.createdTo = createdTo;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "DonationFilterRequest{" +
                "search='" + search + '\'' +
                ", causeName='" + causeName + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", donorName='" + donorName + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", email='" + email + '\'' +
                ", createdFrom=" + createdFrom +
                ", createdTo=" + createdTo +
                '}';
    }
}

