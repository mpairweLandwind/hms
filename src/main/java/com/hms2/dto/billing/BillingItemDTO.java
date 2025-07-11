package com.hms2.dto.billing;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BillingItemDTO {
    
    private Long billingItemId;
    
    @NotNull(message = "Item name is required")
    @Size(max = 200, message = "Item name must not exceed 200 characters")
    private String itemName;
    
    @Size(max = 100, message = "Item code must not exceed 100 characters")
    private String itemCode;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @NotNull(message = "Quantity is required")
    private Integer quantity = 1;
    
    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;
    
    private BigDecimal discountPercentage = BigDecimal.ZERO;
    
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    private BigDecimal taxPercentage = BigDecimal.ZERO;
    
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;
    
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category; // CONSULTATION, MEDICATION, LAB_TEST, PROCEDURE, SURGERY, EQUIPMENT
    
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;
    
    private Boolean isInsuranceCovered = false;
    
    private BigDecimal insuranceAmount = BigDecimal.ZERO;
    
    @Size(max = 100, message = "Insurance code must not exceed 100 characters")
    private String insuranceCode;
    
    // Reference to related entities
    private Long prescriptionId;
    private Long medicalRecordId;
    private Long labTestId;
    private Long procedureId;
    
    // Constructors
    public BillingItemDTO() {}
    
    public BillingItemDTO(String itemName, BigDecimal unitPrice, Integer quantity) {
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        calculateTotalAmount();
    }
    
    // Getters and Setters
    public Long getBillingItemId() {
        return billingItemId;
    }
    
    public void setBillingItemId(Long billingItemId) {
        this.billingItemId = billingItemId;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public String getItemCode() {
        return itemCode;
    }
    
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotalAmount();
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalAmount();
    }
    
    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
        calculateTotalAmount();
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        calculateTotalAmount();
    }
    
    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }
    
    public void setTaxPercentage(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
        calculateTotalAmount();
    }
    
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
        calculateTotalAmount();
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public Boolean getIsInsuranceCovered() {
        return isInsuranceCovered;
    }
    
    public void setIsInsuranceCovered(Boolean isInsuranceCovered) {
        this.isInsuranceCovered = isInsuranceCovered;
    }
    
    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }
    
    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }
    
    public String getInsuranceCode() {
        return insuranceCode;
    }
    
    public void setInsuranceCode(String insuranceCode) {
        this.insuranceCode = insuranceCode;
    }
    
    public Long getPrescriptionId() {
        return prescriptionId;
    }
    
    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }
    
    public Long getMedicalRecordId() {
        return medicalRecordId;
    }
    
    public void setMedicalRecordId(Long medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }
    
    public Long getLabTestId() {
        return labTestId;
    }
    
    public void setLabTestId(Long labTestId) {
        this.labTestId = labTestId;
    }
    
    public Long getProcedureId() {
        return procedureId;
    }
    
    public void setProcedureId(Long procedureId) {
        this.procedureId = procedureId;
    }
    
    // Utility methods
    private void calculateTotalAmount() {
        if (unitPrice != null && quantity != null) {
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            
            // Apply discount
            if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
                discountAmount = subtotal.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
            }
            
            BigDecimal amountAfterDiscount = subtotal.subtract(discountAmount);
            
            // Apply tax
            if (taxPercentage != null && taxPercentage.compareTo(BigDecimal.ZERO) > 0) {
                taxAmount = amountAfterDiscount.multiply(taxPercentage).divide(BigDecimal.valueOf(100));
            }
            
            totalAmount = amountAfterDiscount.add(taxAmount);
        }
    }
    
    public BigDecimal getSubtotal() {
        if (unitPrice != null && quantity != null) {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }
    
    public BigDecimal getAmountAfterDiscount() {
        return getSubtotal().subtract(discountAmount);
    }
    
    @Override
    public String toString() {
        return "BillingItemDTO{" +
                "billingItemId=" + billingItemId +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalAmount=" + totalAmount +
                ", category='" + category + '\'' +
                '}';
    }
} 