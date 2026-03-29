package com.iskcongev.GEV_Donation.dto;

import java.time.LocalDate;


public class DonorFilterRequest {
    private String global;
    private LocalDate createdFrom;
    private LocalDate createdTo;
    private int page = 0;
    private int size = 10;

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

    public String getGlobal() {
        return global;
    }

    public void setGlobal(String global) {
        this.global = global;
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

    @Override
    public String toString() {
        return "DonorFilterRequest{" +
                "global='" + global + '\'' +
                ", createdFrom=" + createdFrom +
                ", createdTo=" + createdTo +
                '}';
    }
}
