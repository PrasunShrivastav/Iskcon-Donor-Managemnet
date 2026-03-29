package com.iskcongev.GEV_Donation.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="options_group")
public class OptionsGroup {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;
    
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

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Options> options = new ArrayList<>();

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

    public OptionsGroup() {
    }

    public OptionsGroup(Long id, String name, Boolean deleted, LocalDateTime created_at, LocalDateTime updated_at,
            LocalDateTime deleted_at, String created_by_user_id, String updated_by_user_id, String deleted_by_user_id,
            List<Options> options) {
        this.id = id;
        this.name = name;
        this.deleted = deleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.created_by_user_id = created_by_user_id;
        this.updated_by_user_id = updated_by_user_id;
        this.deleted_by_user_id = deleted_by_user_id;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }
}
