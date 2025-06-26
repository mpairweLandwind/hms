package com.hms2.controller;

import com.hms2.model.Billing;
import com.hms2.model.Patient;
import com.hms2.model.Appointment;
import com.hms2.service.BillingService;
import com.hms2.service.PatientService;
import com.hms2.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Named("billingController")
@ViewScoped
public class BillingController implements Serializable {
    
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
