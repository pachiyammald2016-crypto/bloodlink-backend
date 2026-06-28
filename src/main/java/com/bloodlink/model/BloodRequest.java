package com.bloodlink.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "blood_requests")
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_name", length = 100, nullable = false)
    private String patientName;

    @Column(name = "blood_group", length = 5, nullable = false)
    private String bloodGroup;

    @Column(name = "hospital", length = 200, nullable = false)
    private String hospital;

    @Column(name = "city", length = 100, nullable = false)
    private String city;

    @Column(name = "landmark", length = 200)
    private String landmark;

    @Column(name = "units", nullable = false)
    private Integer units;

    @Column(name = "urgency", length = 20)
    private String urgency; // Normal, Medium, Critical

    @Column(name = "phone", length = 10, nullable = false)
    private String phone;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "Active"; // Active, Fulfilled, Cancelled

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Constructors
    public BloodRequest() {
    }

    public BloodRequest(String patientName, String bloodGroup, String hospital, String city, String landmark, Integer units, String urgency, String phone, String notes, String status) {
        this.patientName = patientName;
        this.bloodGroup = bloodGroup;
        this.hospital = hospital;
        this.city = city;
        this.landmark = landmark;
        this.units = units;
        this.urgency = urgency;
        this.phone = phone;
        this.notes = notes;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
