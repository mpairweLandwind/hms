package com.hms2.dto.billing;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BillingResponseDTO {
    
    private Long billingId;
    private String billingNumber;
    
    // Patient information
    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String patientAddress;
    
    // Doctor information
    private Long doctorId;
    private String doctorName;
    private String doctorEmail;
    private String doctorPhone;
    private String doctorDepartment;
    
    // Appointment information
    private Long appointmentId;
    private Date appointmentDate;
    private String appointmentType;
    private String appointmentStatus;
    
    // Billing details
    private Date billingDate;
    private Date dueDate;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal balanceAmount;
    private String billingType;
    private String status;
    private String description;
    private String paymentMethod;
    
    // Insurance information
    private String insuranceProvider;
    private String insuranceNumber;
    private Boolean insuranceCovered;
    private BigDecimal insuranceAmount;
    private String policyNumber;
    private Date policyExpiryDate;
    private String claimNumber;
    private Date claimDate;
    private String claimStatus;
    
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
    private String notes;
    private Boolean isUrgent;
    private Boolean isDisputed;
    private String disputeReason;
    private Date disputeDate;
    private String disputeStatus;
    
    // Audit fields
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    
    // Computed fields
    private Boolean isFullyPaid;
    private Boolean isOverdue;
    private Integer daysOverdue;
    private String statusColor;
    private String statusIcon;
    
    // Constructors
    public BillingResponseDTO() {}
    
    // Getters and Setters
    public Long getBillingId() {
        return billingId;
    }
    
    public void setBillingId(Long billingId) {
        this.billingId = billingId;
    }
    
    public String getBillingNumber() {
        return billingNumber;
    }
    
    public void setBillingNumber(String billingNumber) {
        this.billingNumber = billingNumber;
    }
    
    public Long getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    
    public String getPatientName() {
        return patientName;
    }
    
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    
    public String getPatientEmail() {
        return patientEmail;
    }
    
    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
    
    public String getPatientPhone() {
        return patientPhone;
    }
    
    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }
    
    public String getPatientAddress() {
        return patientAddress;
    }
    
    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }
    
    public Long getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
    
    public String getDoctorName() {
        return doctorName;
    }
    
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    
    public String getDoctorEmail() {
        return doctorEmail;
    }
    
    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }
    
    public String getDoctorPhone() {
        return doctorPhone;
    }
    
    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }
    
    public String getDoctorDepartment() {
        return doctorDepartment;
    }
    
    public void setDoctorDepartment(String doctorDepartment) {
        this.doctorDepartment = doctorDepartment;
    }
    
    public Long getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public Date getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public String getAppointmentType() {
        return appointmentType;
    }
    
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }
    
    public String getAppointmentStatus() {
        return appointmentStatus;
    }
    
    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
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
    
    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }
    
    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
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
    
    public Boolean getIsFullyPaid() {
        return isFullyPaid;
    }
    
    public void setIsFullyPaid(Boolean isFullyPaid) {
        this.isFullyPaid = isFullyPaid;
    }
    
    public Boolean getIsOverdue() {
        return isOverdue;
    }
    
    public void setIsOverdue(Boolean isOverdue) {
        this.isOverdue = isOverdue;
    }
    
    public Integer getDaysOverdue() {
        return daysOverdue;
    }
    
    public void setDaysOverdue(Integer daysOverdue) {
        this.daysOverdue = daysOverdue;
    }
    
    public String getStatusColor() {
        return statusColor;
    }
    
    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }
    
    public String getStatusIcon() {
        return statusIcon;
    }
    
    public void setStatusIcon(String statusIcon) {
        this.statusIcon = statusIcon;
    }
    
    // Utility methods
    public void calculateComputedFields() {
        if (totalAmount != null && paidAmount != null) {
            this.balanceAmount = totalAmount.subtract(paidAmount).subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
            this.isFullyPaid = this.balanceAmount.compareTo(BigDecimal.ZERO) <= 0;
        }
        
        if (dueDate != null) {
            Date now = new Date();
            this.isOverdue = dueDate.before(now) && !isFullyPaid;
            
            if (isOverdue) {
                long diffInMillies = now.getTime() - dueDate.getTime();
                this.daysOverdue = (int) (diffInMillies / (24 * 60 * 60 * 1000));
            }
        }
        
        // Set status colors and icons
        if (status != null) {
            switch (status.toUpperCase()) {
                case "PAID":
                    this.statusColor = "success";
                    this.statusIcon = "pi pi-check-circle";
                    break;
                case "PENDING":
                    this.statusColor = "warning";
                    this.statusIcon = "pi pi-clock";
                    break;
                case "OVERDUE":
                    this.statusColor = "danger";
                    this.statusIcon = "pi pi-exclamation-triangle";
                    break;
                case "PARTIAL":
                    this.statusColor = "info";
                    this.statusIcon = "pi pi-dollar";
                    break;
                case "CANCELLED":
                    this.statusColor = "secondary";
                    this.statusIcon = "pi pi-times-circle";
                    break;
                case "REFUNDED":
                    this.statusColor = "info";
                    this.statusIcon = "pi pi-undo";
                    break;
                default:
                    this.statusColor = "primary";
                    this.statusIcon = "pi pi-file";
            }
        }
    }
    
    @Override
    public String toString() {
        return "BillingResponseDTO{" +
                "billingId=" + billingId +
                ", billingNumber='" + billingNumber + '\'' +
                ", patientName='" + patientName + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", isFullyPaid=" + isFullyPaid +
                ", isOverdue=" + isOverdue +
                '}';
    }
} 