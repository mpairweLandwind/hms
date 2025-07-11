package com.hms2.dto.prescription;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MedicationDTO {
    
    private Long medicationId;
    
    @NotNull(message = "Medication name is required")
    @Size(max = 200, message = "Medication name must not exceed 200 characters")
    private String medicationName;
    
    @Size(max = 100, message = "Generic name must not exceed 100 characters")
    private String genericName;
    
    @Size(max = 50, message = "Dosage must not exceed 50 characters")
    private String dosage;
    
    @Size(max = 50, message = "Frequency must not exceed 50 characters")
    private String frequency; // Once daily, Twice daily, etc.
    
    @Size(max = 50, message = "Route must not exceed 50 characters")
    private String route; // Oral, Topical, Injectable, etc.
    
    @Size(max = 20, message = "Duration must not exceed 20 characters")
    private String duration; // 7 days, 14 days, etc.
    
    @Size(max = 500, message = "Instructions must not exceed 500 characters")
    private String instructions;
    
    @Size(max = 100, message = "Side effects must not exceed 100 characters")
    private String sideEffects;
    
    @Size(max = 100, message = "Contraindications must not exceed 100 characters")
    private String contraindications;
    
    private Integer quantity;
    
    @Size(max = 50, message = "Unit must not exceed 50 characters")
    private String unit; // tablets, capsules, ml, etc.
    
    private Double cost;
    
    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    private String manufacturer;
    
    @Size(max = 50, message = "Strength must not exceed 50 characters")
    private String strength; // 500mg, 10ml, etc.
    
    private Boolean isControlled = false;
    
    @Size(max = 100, message = "Special instructions must not exceed 100 characters")
    private String specialInstructions;
    
    // Constructors
    public MedicationDTO() {}
    
    public MedicationDTO(String medicationName, String dosage, String frequency, String duration) {
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
    }
    
    // Getters and Setters
    public Long getMedicationId() {
        return medicationId;
    }
    
    public void setMedicationId(Long medicationId) {
        this.medicationId = medicationId;
    }
    
    public String getMedicationName() {
        return medicationName;
    }
    
    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }
    
    public String getGenericName() {
        return genericName;
    }
    
    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }
    
    public String getDosage() {
        return dosage;
    }
    
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    
    public String getFrequency() {
        return frequency;
    }
    
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    
    public String getRoute() {
        return route;
    }
    
    public void setRoute(String route) {
        this.route = route;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public String getSideEffects() {
        return sideEffects;
    }
    
    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }
    
    public String getContraindications() {
        return contraindications;
    }
    
    public void setContraindications(String contraindications) {
        this.contraindications = contraindications;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public Double getCost() {
        return cost;
    }
    
    public void setCost(Double cost) {
        this.cost = cost;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public String getStrength() {
        return strength;
    }
    
    public void setStrength(String strength) {
        this.strength = strength;
    }
    
    public Boolean getIsControlled() {
        return isControlled;
    }
    
    public void setIsControlled(Boolean isControlled) {
        this.isControlled = isControlled;
    }
    
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
    
    @Override
    public String toString() {
        return "MedicationDTO{" +
                "medicationId=" + medicationId +
                ", medicationName='" + medicationName + '\'' +
                ", dosage='" + dosage + '\'' +
                ", frequency='" + frequency + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
} 