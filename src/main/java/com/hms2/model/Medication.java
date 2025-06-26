package com.hms2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "medications")
public class Medication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medication_seq")
    @SequenceGenerator(name = "medication_seq", sequenceName = "medication_seq", allocationSize = 1)
    @Column(name = "medication_id")
    private Long medicationId;

    @NotBlank(message = "Medication name is required")
    @Size(max = 100, message = "Medication name must not exceed 100 characters")
    @Column(name = "medication_name", nullable = false, length = 100)
    private String medicationName;

    @Size(max = 100, message = "Generic name must not exceed 100 characters")
    @Column(name = "generic_name", length = 100)
    private String genericName;

    @Size(max = 100, message = "Brand name must not exceed 100 characters")
    @Column(name = "brand_name", length = 100)
    private String brandName;

    @Size(max = 50, message = "Dosage form must not exceed 50 characters")
    @Column(name = "dosage_form", length = 50)
    private String dosageForm;

    @Size(max = 50, message = "Strength must not exceed 50 characters")
    @Column(name = "strength", length = 50)
    private String strength;

    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;

    @DecimalMin(value = "0.0", message = "Unit price must be positive")
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Min(value = 0, message = "Stock quantity must be non-negative")
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Min(value = 0, message = "Minimum stock level must be non-negative")
    @Column(name = "minimum_stock_level")
    private Integer minimumStockLevel = 0;

    @Pattern(regexp = "AVAILABLE|OUT_OF_STOCK|DISCONTINUED|RECALLED",
             message = "Status must be AVAILABLE, OUT_OF_STOCK, DISCONTINUED, or RECALLED")
    @Column(name = "status", length = 20)
    private String status = "AVAILABLE";

    @Size(max = 500, message = "Side effects must not exceed 500 characters")
    @Column(name = "side_effects", length = 500)
    private String sideEffects;

    @Size(max = 500, message = "Contraindications must not exceed 500 characters")
    @Column(name = "contraindications", length = 500)
    private String contraindications;

    @Size(max = 500, message = "Storage instructions must not exceed 500 characters")
    @Column(name = "storage_instructions", length = 500)
    private String storageInstructions;

    // One-to-many relationship with PrescriptionMedication
    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PrescriptionMedication> prescriptionMedications;

    // Constructors
    public Medication() {}

    public Medication(String medicationName, String dosageForm, String strength, BigDecimal unitPrice) {
        this.medicationName = medicationName;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.unitPrice = unitPrice;
    }

    // Business methods
    public boolean isAvailable() {
        return "AVAILABLE".equals(status) && stockQuantity > 0;
    }

    public boolean isLowStock() {
        return stockQuantity <= minimumStockLevel;
    }

    public void updateStock(int quantity) {
        this.stockQuantity += quantity;
        if (this.stockQuantity <= 0) {
            this.status = "OUT_OF_STOCK";
        } else if ("OUT_OF_STOCK".equals(this.status)) {
            this.status = "AVAILABLE";
        }
    }

    public void discontinue() {
        this.status = "DISCONTINUED";
    }

    public void recall() {
        this.status = "RECALLED";
    }

    // Getters and setters
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getMinimumStockLevel() {
        return minimumStockLevel;
    }

    public void setMinimumStockLevel(Integer minimumStockLevel) {
        this.minimumStockLevel = minimumStockLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getStorageInstructions() {
        return storageInstructions;
    }

    public void setStorageInstructions(String storageInstructions) {
        this.storageInstructions = storageInstructions;
    }

    public List<PrescriptionMedication> getPrescriptionMedications() {
        return prescriptionMedications;
    }

    public void setPrescriptionMedications(List<PrescriptionMedication> prescriptionMedications) {
        this.prescriptionMedications = prescriptionMedications;
    }
}
