package com.iskcongev.GEV_Donation.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="donor")
public class Donor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "prefix_id", nullable = false)
    @JsonIgnore
    private Options prefix;

    @Column(name = "first_name", nullable = false)
    private String first_name;

    @Column(name = "last_name", nullable = false)
    private String last_name;

    @Column(name = "email",unique=true, nullable = true)
    private String email;

    @Column(name = "contactNo", nullable = false)
    private String contactNo;

    @Column(name = "alt_contact_no", nullable = true)
    private String alt_contact_no;

    @Column(name = "pan_number", nullable = true)
    private String pan_number;

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

    @Column(name = "facilitator", nullable = true)
    private String facilitator;

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

    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Donation> donations = new ArrayList<>();

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

    public Donor(Long id, Options prefix, String first_name, String last_name, String email, String contactNo,
        String alt_contact_no, String pan_number, String address_line_1, String address_line_2, String city,
        String state, String pincode, String facilitator, Boolean deleted, LocalDateTime createdAt,
        LocalDateTime updated_at, LocalDateTime deleted_at, String created_by_user_id, String updated_by_user_id,
        String deleted_by_user_id, List<Donation> donations) {
        this.id = id;
        this.prefix = prefix;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.contactNo = contactNo;
        this.alt_contact_no = alt_contact_no;
        this.pan_number = pan_number;
        this.address_line_1 = address_line_1;
        this.address_line_2 = address_line_2;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.facilitator = facilitator;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.created_by_user_id = created_by_user_id;
        this.updated_by_user_id = updated_by_user_id;
        this.deleted_by_user_id = deleted_by_user_id;
        this.donations = donations;
    }

    public Donor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Options getPrefix() {
        return prefix;
    }

    public void setPrefix(Options prefix) {
        this.prefix = prefix;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_no() {
        return contactNo;
    }

    public void setContact_no(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAlt_contact_no() {
        return alt_contact_no;
    }

    public void setAlt_contact_no(String alt_contact_no) {
        this.alt_contact_no = alt_contact_no;
    }

    public String getPan_number() {
        return pan_number;
    }

    public void setPan_number(String pan_number) {
        this.pan_number = pan_number;
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

    public String getFacilitator() {
        return facilitator;
    }

    public void setFacilitator(String facilitator) {
        this.facilitator = facilitator;
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

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }

}
