package com.iskcongev.GEV_Donation.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import jakarta.persistence.Transient;

@Entity
@Table(name = "cause")
public class Cause {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private CauseCategory category;

    @Column(name = "display_name", nullable = false)
    private String display_name;

    @Column(name = "default_amount", nullable = false)
    private Double default_amount;

    @Column(name = "image_url", nullable = true)
    private String image_url;

    @Column(name = "image_url_sm", nullable = true)
    private String image_url_sm;

    private Boolean deleted;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

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

    @OneToMany(mappedBy = "cause", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Donation> donations = new ArrayList<>();

    @Transient
    public String getCategoryName() {
        return category.getParent_id() == null ? category.getName() : category.getParent_id();
    }

    @Transient
    public String getSubCategoryName() {
        return category != null ? category.getName() : null;
    }

    public Cause() {
    }

    @PrePersist
    protected void onCreate() {
        if (created_at == null) {
            created_at = LocalDateTime.now();
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

    public Cause(CauseCategory category, LocalDateTime created_at, String created_by_user_id, Double default_amount, Boolean deleted, LocalDateTime deleted_at, String deleted_by_user_id, String display_name, Long id, String image_url, String image_url_sm, LocalDateTime updated_at, String updated_by_user_id) {
        this.category = category;
        this.created_at = created_at;
        this.created_by_user_id = created_by_user_id;
        this.default_amount = default_amount;
        this.deleted = deleted;
        this.deleted_at = deleted_at;
        this.deleted_by_user_id = deleted_by_user_id;
        this.display_name = display_name;
        this.id = id;
        this.image_url = image_url;
        this.image_url_sm = image_url_sm;
        this.updated_at = updated_at;
        this.updated_by_user_id = updated_by_user_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CauseCategory getCategory() {
        return category;
    }

    public void setCategory(CauseCategory category) {
        this.category = category;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Double getDefault_amount() {
        return default_amount;
    }

    public void setDefault_amount(Double default_amount) {
        this.default_amount = default_amount;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url_sm() {
        return image_url_sm;
    }

    public void setImage_url_sm(String image_url_sm) {
        this.image_url_sm = image_url_sm;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
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

