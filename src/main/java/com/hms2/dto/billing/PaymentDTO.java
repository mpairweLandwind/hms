package com.hms2.dto.billing;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PaymentDTO {
    
    private Long paymentId;
    
    @NotNull(message = "Billing ID is required")
    private Long billingId;
    
    @NotNull(message = "Payment amount is required")
    private BigDecimal amount;
    
    @NotNull(message = "Payment date is required")
    private Date paymentDate;
    
    @NotNull(message = "Payment method is required")
    @Size(max = 100, message = "Payment method must not exceed 100 characters")
    private String paymentMethod; // CASH, CARD, INSURANCE, BANK_TRANSFER, ONLINE, CHECK
    
    @Size(max = 100, message = "Transaction ID must not exceed 100 characters")
    private String transactionId;
    
    @Size(max = 100, message = "Reference number must not exceed 100 characters")
    private String referenceNumber;
    
    @Size(max = 100, message = "Card type must not exceed 100 characters")
    private String cardType; // VISA, MASTERCARD, AMEX, DISCOVER
    
    @Size(max = 100, message = "Card last digits must not exceed 100 characters")
    private String cardLastDigits;
    
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;
    
    @Size(max = 100, message = "Account number must not exceed 100 characters")
    private String accountNumber;
    
    @Size(max = 100, message = "Check number must not exceed 100 characters")
    private String checkNumber;
    
    @Size(max = 100, message = "Insurance provider must not exceed 100 characters")
    private String insuranceProvider;
    
    @Size(max = 100, message = "Claim number must not exceed 100 characters")
    private String claimNumber;
    
    @Size(max = 100, message = "Status must not exceed 100 characters")
    private String status = "PENDING"; // PENDING, COMPLETED, FAILED, CANCELLED, REFUNDED
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
    
    @Size(max = 100, message = "Processed by must not exceed 100 characters")
    private String processedBy;
    
    private Date processedDate;
    
    @Size(max = 100, message = "Approved by must not exceed 100 characters")
    private String approvedBy;
    
    private Date approvedDate;
    
    @Size(max = 100, message = "Rejected by must not exceed 100 characters")
    private String rejectedBy;
    
    private Date rejectedDate;
    
    @Size(max = 500, message = "Rejection reason must not exceed 500 characters")
    private String rejectionReason;
    
    // Online payment specific fields
    @Size(max = 100, message = "Gateway must not exceed 100 characters")
    private String gateway; // STRIPE, PAYPAL, SQUARE, etc.
    
    @Size(max = 100, message = "Gateway transaction ID must not exceed 100 characters")
    private String gatewayTransactionId;
    
    @Size(max = 100, message = "Gateway status must not exceed 100 characters")
    private String gatewayStatus;
    
    @Size(max = 1000, message = "Gateway response must not exceed 1000 characters")
    private String gatewayResponse;
    
    // Refund information
    private Boolean isRefunded = false;
    
    private BigDecimal refundAmount = BigDecimal.ZERO;
    
    private Date refundDate;
    
    @Size(max = 100, message = "Refunded by must not exceed 100 characters")
    private String refundedBy;
    
    @Size(max = 500, message = "Refund reason must not exceed 500 characters")
    private String refundReason;
    
    // Audit fields
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    
    // Constructors
    public PaymentDTO() {}
    
    public PaymentDTO(Long billingId, BigDecimal amount, String paymentMethod) {
        this.billingId = billingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = new Date();
    }
    
    // Getters and Setters
    public Long getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    
    public Long getBillingId() {
        return billingId;
    }
    
    public void setBillingId(Long billingId) {
        this.billingId = billingId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Date getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    
    public String getCardType() {
        return cardType;
    }
    
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
    
    public String getCardLastDigits() {
        return cardLastDigits;
    }
    
    public void setCardLastDigits(String cardLastDigits) {
        this.cardLastDigits = cardLastDigits;
    }
    
    public String getBankName() {
        return bankName;
    }
    
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public String getCheckNumber() {
        return checkNumber;
    }
    
    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }
    
    public String getInsuranceProvider() {
        return insuranceProvider;
    }
    
    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }
    
    public String getClaimNumber() {
        return claimNumber;
    }
    
    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getProcessedBy() {
        return processedBy;
    }
    
    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }
    
    public Date getProcessedDate() {
        return processedDate;
    }
    
    public void setProcessedDate(Date processedDate) {
        this.processedDate = processedDate;
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
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    
    public String getGateway() {
        return gateway;
    }
    
    public void setGateway(String gateway) {
        this.gateway = gateway;
    }
    
    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }
    
    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }
    
    public String getGatewayStatus() {
        return gatewayStatus;
    }
    
    public void setGatewayStatus(String gatewayStatus) {
        this.gatewayStatus = gatewayStatus;
    }
    
    public String getGatewayResponse() {
        return gatewayResponse;
    }
    
    public void setGatewayResponse(String gatewayResponse) {
        this.gatewayResponse = gatewayResponse;
    }
    
    public Boolean getIsRefunded() {
        return isRefunded;
    }
    
    public void setIsRefunded(Boolean isRefunded) {
        this.isRefunded = isRefunded;
    }
    
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }
    
    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }
    
    public Date getRefundDate() {
        return refundDate;
    }
    
    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }
    
    public String getRefundedBy() {
        return refundedBy;
    }
    
    public void setRefundedBy(String refundedBy) {
        this.refundedBy = refundedBy;
    }
    
    public String getRefundReason() {
        return refundReason;
    }
    
    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
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
    public Boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    public Boolean isPending() {
        return "PENDING".equals(status);
    }
    
    public Boolean isFailed() {
        return "FAILED".equals(status);
    }
    
    public Boolean isRefundable() {
        return isCompleted() && !isRefunded;
    }
    
    public BigDecimal getNetAmount() {
        return amount.subtract(refundAmount);
    }
    
    @Override
    public String toString() {
        return "PaymentDTO{" +
                "paymentId=" + paymentId +
                ", billingId=" + billingId +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }
} 