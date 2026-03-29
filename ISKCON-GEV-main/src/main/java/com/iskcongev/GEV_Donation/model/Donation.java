package com.iskcongev.GEV_Donation.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="donation")
public class Donation {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Donor donor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cause_id", nullable = false)
    @JsonIgnore
    @JsonIgnoreProperties({"donations", "category", "hibernateLazyInitializer", "handler"})
    private Cause cause;

    @Column(name = "amount", nullable = false)    
    private Double amount;

    @Column(name = "quantity", nullable = false)    
    private Double quantity;

    @ManyToOne
    @JoinColumn(name = "payment_mode_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Options paymentMode;

    @ManyToOne
    @JoinColumn(name = "purpose_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Options purpose;

    @Column(name = "receipt_id", nullable = true)
    private String receipt_id;

    @Column(name = "address_line_1", nullable = true)
    private String address_line_1;

    @Column(name = "address_line_2", nullable = true)
    private String address_line_2;

    @Column(name = "city", nullable = true)
    private String city;

    @Column(name = "state", nullable = true)
    private String state;

    @Column(name = "pincode", nullable = true)
    private String pincode;

    private Boolean deleted;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updated_at;

    @Column(nullable = true)
    private LocalDateTime deleted_at;

    @Column(name = "created_by_user_id")
    private String created_by_user_id;

    @Column(name = "updated_by_user_id", nullable = true)
    private String updated_by_user_id;

    @Column(name = "deleted_by_user_id")
    private String deleted_by_user_id;


    @Transient
    public String getCategoryName() {
        return cause != null ? cause.getCategoryName() : null;
    }

    @Transient
    public String getSubCategoryName() {
        return cause != null ? cause.getSubCategoryName() : null;
    }

    @Transient
    public String getCauseName() {
        return cause != null ? cause.getDisplay_name() : null;
    }

    

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updated_at == null) {
            updated_at = LocalDateTime.now();
        }
        if (deleted == null) {
            deleted = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }

    public Donation() {
    }

    public Donation(Long id, Donor donor, Cause cause, Double amount, Double quantity, Options paymentMode, Options purpose,
            String receipt_id, String address_line_1, String address_line_2, String city, String state, String pincode,
            Boolean deleted, LocalDateTime createdAt, LocalDateTime updated_at, LocalDateTime deleted_at,
            String created_by_user_id, String updated_by_user_id, String deleted_by_user_id) {
        this.id = id;
        this.donor = donor;
        this.cause = cause;
        this.amount = amount;
        this.quantity = quantity;
        this.paymentMode = paymentMode;
        this.purpose = purpose;
        this.receipt_id = receipt_id;
        this.address_line_1 = address_line_1;
        this.address_line_2 = address_line_2;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.created_by_user_id = created_by_user_id;
        this.updated_by_user_id = updated_by_user_id;
        this.deleted_by_user_id = deleted_by_user_id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public Cause getCauseId() {
        return cause;
    }

    public void setCauseId(Cause cause) {
        this.cause = cause;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Options getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Options paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Options getPurpose() {
        return purpose;
    }

    public void setPurpose(Options purpose) {
        this.purpose = purpose;
    }

    public String getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(String receipt_id) {
        this.receipt_id = receipt_id;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreated_at() {
        return createdAt;
    }

    public void setCreated_at(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public LocalDateTime getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getCreated_by_user_id() {
        return created_by_user_id;
    }

    public void setCreated_by_user_id(String created_by_user_id) {
        this.created_by_user_id = created_by_user_id;
    }

    public String getUpdated_by_user_id() {
        return updated_by_user_id;
    }

    public void setUpdated_by_user_id(String updated_by_user_id) {
        this.updated_by_user_id = updated_by_user_id;
    }

    public String getDeleted_by_user_id() {
        return deleted_by_user_id;
    }

    public void setDeleted_by_user_id(String deleted_by_user_id) {
        this.deleted_by_user_id = deleted_by_user_id;
    }


 
}
