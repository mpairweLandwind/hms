package com.hms2.service;

import com.hms2.model.Billing;
import com.hms2.model.Patient;
import com.hms2.model.Appointment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface BillingService {
    
    Billing createBilling(Billing billing);
    
    Billing updateBilling(Billing billing);
    
    void deleteBilling(Long billingId);
    
    Billing getBillingById(Long billingId);
    
    Billing getBillingByInvoiceNumber(String invoiceNumber);
    
    List<Billing> getAllBillings();
    
    List<Billing> getBillingsByPatient(Long patientId);
    
    List<Billing> getBillingsByAppointment(Long appointmentId);
    
    List<Billing> getBillingsByStatus(String status);
    
    List<Billing> getBillingsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Billing> getPendingBills();
    
    List<Billing> getPendingBillsByPatient(Long patientId);
    
    List<Billing> getOverdueBillings();
    
    void processPayment(Long billingId, BigDecimal amount, String paymentMethod);
    
    void markAsPaid(Long billingId);
    
    void cancelBilling(Long billingId);
    
    String generateInvoiceNumber();
    
    BigDecimal calculateTotalRevenue();
    
    BigDecimal calculatePendingAmount();
    
    long getCountByStatus(String status);
    
    List<Billing> getRecentBillings(int limit);
}
