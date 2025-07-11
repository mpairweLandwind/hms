package com.hms2.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.model.Appointment;
import com.hms2.model.Billing;
import com.hms2.model.Patient;
import com.hms2.service.AppointmentService;
import com.hms2.service.BillingService;
import com.hms2.service.PatientService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

@Named("billingController")
@RequestScoped
public class BillingController {
    
    private static final Logger logger = LoggerFactory.getLogger(BillingController.class);
    
    @Inject
    private BillingService billingService;
    
    @Inject
    private PatientService patientService;
    
    @Inject
    private AppointmentService appointmentService;
    
    private List<Billing> billings;
    private List<Patient> patients;
    private List<Appointment> appointments;
    private Billing selectedBilling;
    private Billing newBilling;
    
    // Payment processing
    private BigDecimal paymentAmount;
    private String paymentMethod;
    
    // Filters
    private String statusFilter;
    private String patientFilter;
    private LocalDateTime startDateFilter;
    private LocalDateTime endDateFilter;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing billing controller");
        loadData();
        initNewBilling();
    }
    
    private void loadData() {
        try {
            billings = billingService.getAllBillings();
            patients = patientService.getAllPatients();
            appointments = appointmentService.getAllAppointments();
            logger.info("Loaded {} billings", billings.size());
        } catch (Exception e) {
            logger.error("Error loading billing data", e);
            addErrorMessage("Error loading billing data: " + e.getMessage());
        }
    }
    
    private void initNewBilling() {
        newBilling = new Billing();
        newBilling.setBillingDate(LocalDateTime.now());
        newBilling.setStatus("PENDING");
        newBilling.setPaidAmount(BigDecimal.ZERO);
        newBilling.setDiscountAmount(BigDecimal.ZERO);
        newBilling.setTaxAmount(BigDecimal.ZERO);
    }
    
    public void createBilling() {
        try {
            billingService.createBilling(newBilling);
            addSuccessMessage("Billing created successfully");
            loadData();
            initNewBilling();
        } catch (Exception e) {
            logger.error("Error creating billing", e);
            addErrorMessage("Error creating billing: " + e.getMessage());
        }
    }
    
    public void updateBilling() {
        try {
            billingService.updateBilling(selectedBilling);
            addSuccessMessage("Billing updated successfully");
            loadData();
        } catch (Exception e) {
            logger.error("Error updating billing", e);
            addErrorMessage("Error updating billing: " + e.getMessage());
        }
    }
    
    public void deleteBilling(Billing billing) {
        try {
            billingService.deleteBilling(billing.getBillingId());
            addSuccessMessage("Billing deleted successfully");
            loadData();
        } catch (Exception e) {
            logger.error("Error deleting billing", e);
            addErrorMessage("Error deleting billing: " + e.getMessage());
        }
    }
    
    public void processPayment() {
        try {
            if (selectedBilling != null && paymentAmount != null && paymentAmount.compareTo(BigDecimal.ZERO) > 0) {
                billingService.processPayment(selectedBilling.getBillingId(), paymentAmount, paymentMethod);
                addSuccessMessage("Payment processed successfully");
                loadData();
                clearPaymentForm();
            } else {
                addErrorMessage("Please enter a valid payment amount");
            }
        } catch (Exception e) {
            logger.error("Error processing payment", e);
            addErrorMessage("Error processing payment: " + e.getMessage());
        }
    }
    
    public void markAsPaid(Billing billing) {
        try {
            billingService.markAsPaid(billing.getBillingId());
            addSuccessMessage("Billing marked as paid");
            loadData();
        } catch (Exception e) {
            logger.error("Error marking billing as paid", e);
            addErrorMessage("Error marking billing as paid: " + e.getMessage());
        }
    }
    
    public void cancelBilling(Billing billing) {
        try {
            billingService.cancelBilling(billing.getBillingId());
            addSuccessMessage("Billing cancelled successfully");
            loadData();
        } catch (Exception e) {
            logger.error("Error cancelling billing", e);
            addErrorMessage("Error cancelling billing: " + e.getMessage());
        }
    }
    
    public void applyFilters() {
        try {
            List<Billing> filteredBillings = billingService.getAllBillings();
            
            if (statusFilter != null && !statusFilter.isEmpty()) {
                filteredBillings = filteredBillings.stream()
                    .filter(b -> statusFilter.equals(b.getStatus()))
                    .toList();
            }
            
            if (startDateFilter != null && endDateFilter != null) {
                filteredBillings = billingService.getBillingsByDateRange(startDateFilter, endDateFilter);
            }
            
            billings = filteredBillings;
            logger.info("Applied filters, showing {} billings", billings.size());
            
        } catch (Exception e) {
            logger.error("Error applying filters", e);
            addErrorMessage("Error applying filters: " + e.getMessage());
        }
    }
    
    public void clearFilters() {
        statusFilter = null;
        patientFilter = null;
        startDateFilter = null;
        endDateFilter = null;
        loadData();
    }
    
    // Statistics methods for dashboard
    public BigDecimal calculateTotalRevenue() {
        try {
            return billings.stream()
                .filter(b -> "PAID".equals(b.getStatus()))
                .map(Billing::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            logger.error("Error calculating total revenue", e);
            return BigDecimal.ZERO;
        }
    }
    
    public BigDecimal calculatePendingAmount() {
        try {
            return billings.stream()
                .filter(b -> "PENDING".equals(b.getStatus()) || "PARTIALLY_PAID".equals(b.getStatus()))
                .map(Billing::getBalanceAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            logger.error("Error calculating pending amount", e);
            return BigDecimal.ZERO;
        }
    }
    
    public long getPendingBillingCount() {
        try {
            return billings.stream()
                .filter(b -> "PENDING".equals(b.getStatus()))
                .count();
        } catch (Exception e) {
            logger.error("Error counting pending billings", e);
            return 0;
        }
    }
    
    public long getOverdueBillingCount() {
        try {
            return billings.stream()
                .filter(b -> "OVERDUE".equals(b.getStatus()) || b.isOverdue())
                .count();
        } catch (Exception e) {
            logger.error("Error counting overdue billings", e);
            return 0;
        }
    }
    
    public long getPaidBillingCount() {
        try {
            return billings.stream()
                .filter(b -> "PAID".equals(b.getStatus()))
                .count();
        } catch (Exception e) {
            logger.error("Error counting paid billings", e);
            return 0;
        }
    }
    
    private void clearPaymentForm() {
        paymentAmount = null;
        paymentMethod = null;
    }
    
    public void selectBilling(Billing billing) {
        this.selectedBilling = billing;
    }
    
    public String getStatusColor(String status) {
        switch (status) {
            case "PAID":
                return "success";
            case "PENDING":
                return "warning";
            case "OVERDUE":
                return "danger";
            case "PARTIALLY_PAID":
                return "info";
            case "CANCELLED":
                return "secondary";
            default:
                return "primary";
        }
    }
    
    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }
    
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }
    
    // Getters and setters
    public List<Billing> getBillings() { return billings; }
    public void setBillings(List<Billing> billings) { this.billings = billings; }
    
    public List<Patient> getPatients() { return patients; }
    public void setPatients(List<Patient> patients) { this.patients = patients; }
    
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
    
    public Billing getSelectedBilling() { return selectedBilling; }
    public void setSelectedBilling(Billing selectedBilling) { this.selectedBilling = selectedBilling; }
    
    public Billing getNewBilling() { return newBilling; }
    public void setNewBilling(Billing newBilling) { this.newBilling = newBilling; }
    
    public BigDecimal getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getStatusFilter() { return statusFilter; }
    public void setStatusFilter(String statusFilter) { this.statusFilter = statusFilter; }
    
    public String getPatientFilter() { return patientFilter; }
    public void setPatientFilter(String patientFilter) { this.patientFilter = patientFilter; }
    
    public LocalDateTime getStartDateFilter() { return startDateFilter; }
    public void setStartDateFilter(LocalDateTime startDateFilter) { this.startDateFilter = startDateFilter; }
    
    public LocalDateTime getEndDateFilter() { return endDateFilter; }
    public void setEndDateFilter(LocalDateTime endDateFilter) { this.endDateFilter = endDateFilter; }
}
