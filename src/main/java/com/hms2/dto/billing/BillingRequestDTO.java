package com.hms2.dto.billing;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BillingRequestDTO {
    
    private Long billingId;
    
    @NotNull(message = "Patient is required")
    private Long patientId;
    
    @NotNull(message = "Doctor is required")
    private Long doctorId;
    
    @NotNull(message = "Appointment is required")
    private Long appointmentId;
    
    @NotNull(message = "Billing date is required")
    private Date billingDate;
    
    @NotNull(message = "Due date is required")
    private Date dueDate;
    
    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;
    
    private BigDecimal paidAmount = BigDecimal.ZERO;
    
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @Size(max = 100, message = "Billing type must not exceed 100 characters")
    private String billingType = "CONSULTATION"; // CONSULTATION, PROCEDURE, MEDICATION, LAB_TEST, SURGERY
    
    @Size(max = 100, message = "Status must not exceed 100 characters")
    private String status = "PENDING"; // PENDING, PAID, PARTIAL, OVERDUE, CANCELLED, REFUNDED
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @Size(max = 100, message = "Payment method must not exceed 100 characters")
    private String paymentMethod; // CASH, CARD, INSURANCE, BANK_TRANSFER, ONLINE
    
    @Size(max = 100, message = "Insurance provider must not exceed 100 characters")
    private String insuranceProvider;
    
    @Size(max = 100, message = "Insurance number must not exceed 100 characters")
    private String insuranceNumber;
    
    private Boolean insuranceCovered = false;
    
    private BigDecimal insuranceAmount = BigDecimal.ZERO;
    
    @Size(max = 100, message = "Policy number must not exceed 100 characters")
    private String policyNumber;
    
    private Date policyExpiryDate;
    
    @Size(max = 100, message = "Claim number must not exceed 100 characters")
    private String claimNumber;
    
    private Date claimDate;
    
    @Size(max = 100, message = "Claim status must not exceed 100 characters")
    private String claimStatus; // PENDING, APPROVED, REJECTED, PROCESSING
    
    // Billing items
    private List<BillingItemDTO> billingItems;
    
    // Payment history
    private List<PaymentDTO> payments;
    
    // Approval workflow
    private String approvedBy;
    private Date approvedDate;
    private String approvedByRole;
    private String approvalNotes;
    
    private String rejectedBy;
    private Date rejectedDate;
    private String rejectedByRole;
    private String rejectionReason;
    
    // Additional fields
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
    
    private Boolean isUrgent = false;
    
    private Boolean isDisputed = false;
    
    @Size(max = 500, message = "Dispute reason must not exceed 500 characters")
    private String disputeReason;
    
    private Date disputeDate;
    
    @Size(max = 100, message = "Dispute status must not exceed 100 characters")
    private String disputeStatus; // PENDING, RESOLVED, ESCALATED
    
    // Audit fields
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    
    // Constructors
    public BillingRequestDTO() {}
    
    public BillingRequestDTO(Long patientId, Long doctorId, Long appointmentId, Date billingDate, BigDecimal totalAmount) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.billingDate = billingDate;
        this.totalAmount = totalAmount;
    }
    
    // Getters and Setters
    public Long getBillingId() {
        return billingId;
    }
    
    public void setBillingId(Long billingId) {
        this.billingId = billingId;
    }
    
    public Long getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    
    public Long getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
    
    public Long getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public Date getBillingDate() {
        return billingDate;
    }
    
    public void setBillingDate(Date billingDate) {
        this.billingDate = billingDate;
    }
    
    public Date getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }
    
    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    public String getBillingType() {
        return billingType;
    }
    
    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getInsuranceProvider() {
        return insuranceProvider;
    }
    
    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }
    
    public String getInsuranceNumber() {
        return insuranceNumber;
    }
    
    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }
    
    public Boolean getInsuranceCovered() {
        return insuranceCovered;
    }
    
    public void setInsuranceCovered(Boolean insuranceCovered) {
        this.insuranceCovered = insuranceCovered;
    }
    
    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }
    
    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }
    
    public String getPolicyNumber() {
        return policyNumber;
    }
    
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
    
    public Date getPolicyExpiryDate() {
        return policyExpiryDate;
    }
    
    public void setPolicyExpiryDate(Date policyExpiryDate) {
        this.policyExpiryDate = policyExpiryDate;
    }
    
    public String getClaimNumber() {
        return claimNumber;
    }
    
    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }
    
    public Date getClaimDate() {
        return claimDate;
    }
    
    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }
    
    public String getClaimStatus() {
        return claimStatus;
    }
    
    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }
    
    public List<BillingItemDTO> getBillingItems() {
        return billingItems;
    }
    
    public void setBillingItems(List<BillingItemDTO> billingItems) {
        this.billingItems = billingItems;
    }
    
    public List<PaymentDTO> getPayments() {
        return payments;
    }
    
    public void setPayments(List<PaymentDTO> payments) {
        this.payments = payments;
    }
    
    public String getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public Date getApprovedDate() {
        return approvedDate;
    }
    
    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }
    
    public String getApprovedByRole() {
        return approvedByRole;
    }
    
    public void setApprovedByRole(String approvedByRole) {
        this.approvedByRole = approvedByRole;
    }
    
    public String getApprovalNotes() {
        return approvalNotes;
    }
    
    public void setApprovalNotes(String approvalNotes) {
        this.approvalNotes = approvalNotes;
    }
    
    public String getRejectedBy() {
        return rejectedBy;
    }
    
    public void setRejectedBy(String rejectedBy) {
        this.rejectedBy = rejectedBy;
    }
    
    public Date getRejectedDate() {
        return rejectedDate;
    }
    
    public void setRejectedDate(Date rejectedDate) {
        this.rejectedDate = rejectedDate;
    }
    
    public String getRejectedByRole() {
        return rejectedByRole;
    }
    
    public void setRejectedByRole(String rejectedByRole) {
        this.rejectedByRole = rejectedByRole;
    }
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Boolean getIsUrgent() {
        return isUrgent;
    }
    
    public void setIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
    }
    
    public Boolean getIsDisputed() {
        return isDisputed;
    }
    
    public void setIsDisputed(Boolean isDisputed) {
        this.isDisputed = isDisputed;
    }
    
    public String getDisputeReason() {
        return disputeReason;
    }
    
    public void setDisputeReason(String disputeReason) {
        this.disputeReason = disputeReason;
    }
    
    public Date getDisputeDate() {
        return disputeDate;
    }
    
    public void setDisputeDate(Date disputeDate) {
        this.disputeDate = disputeDate;
    }
    
    public String getDisputeStatus() {
        return disputeStatus;
    }
    
    public void setDisputeStatus(String disputeStatus) {
        this.disputeStatus = disputeStatus;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
    
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    // Utility methods
    public BigDecimal getBalanceAmount() {
        return totalAmount.subtract(paidAmount).subtract(discountAmount);
    }
    
    public Boolean isFullyPaid() {
        return getBalanceAmount().compareTo(BigDecimal.ZERO) <= 0;
    }
    
    public Boolean isOverdue() {
        return dueDate != null && dueDate.before(new Date()) && !isFullyPaid();
    }
    
    @Override
    public String toString() {
        return "BillingRequestDTO{" +
                "billingId=" + billingId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", appointmentId=" + appointmentId +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", billingType='" + billingType + '\'' +
                '}';
    }
} 