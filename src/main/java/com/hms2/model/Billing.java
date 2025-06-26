package com.hms2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "billings")
public class Billing extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing_seq")
    @SequenceGenerator(name = "billing_seq", sequenceName = "billing_seq", allocationSize = 1)
    @Column(name = "billing_id")
    private Long billingId;

    // Many-to-one relationship with Patient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Patient is required")
    private Patient patient;

    // Many-to-one relationship with Appointment (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @NotBlank(message = "Invoice number is required")
    @Size(max = 50, message = "Invoice number must not exceed 50 characters")
    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    @NotNull(message = "Billing date is required")
    @Column(name = "billing_date", nullable = false)
    private LocalDateTime billingDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be positive")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @DecimalMin(value = "0.0", message = "Paid amount must be positive")
    @Column(name = "paid_amount", precision = 10, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Discount amount must be positive")
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Tax amount must be positive")
    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Pattern(regexp = "PENDING|PAID|PARTIALLY_PAID|OVERDUE|CANCELLED",
             message = "Status must be PENDING, PAID, PARTIALLY_PAID, OVERDUE, or CANCELLED")
    @Column(name = "status", length = 20)
    private String status = "PENDING";

    @Pattern(regexp = "CASH|CREDIT_CARD|DEBIT_CARD|INSURANCE|BANK_TRANSFER|CHECK",
             message = "Payment method must be CASH, CREDIT_CARD, DEBIT_CARD, INSURANCE, BANK_TRANSFER, or CHECK")
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Column(name = "notes", length = 500)
    private String notes;

    // Constructors
    public Billing() {}

    public Billing(Patient patient, String invoiceNumber, LocalDateTime billingDate, BigDecimal totalAmount) {
        this.patient = patient;
        this.invoiceNumber = invoiceNumber;
        this.billingDate = billingDate;
        this.totalAmount = totalAmount;
    }

    // Business methods
    public BigDecimal getBalanceAmount() {
        return totalAmount.subtract(paidAmount);
    }

    public boolean isPaid() {
        return "PAID".equals(status);
    }

    public boolean isOverdue() {
        return "OVERDUE".equals(status) || 
               (dueDate != null && dueDate.isBefore(LocalDateTime.now()) && !isPaid());
    }

    public void markAsPaid() {
        this.status = "PAID";
        this.paidAmount = this.totalAmount;
        this.paymentDate = LocalDateTime.now();
    }

    public void addPayment(BigDecimal amount, String paymentMethod) {
        this.paidAmount = this.paidAmount.add(amount);
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();
        
        if (this.paidAmount.compareTo(this.totalAmount) >= 0) {
            this.status = "PAID";
        } else {
            this.status = "PARTIALLY_PAID";
        }
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    // Getters and setters
    public Long getBillingId() {
        return billingId;
    }

    public void setBillingId(Long billingId) {
        this.billingId = billingId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDateTime getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDateTime billingDate) {
        this.billingDate = billingDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
