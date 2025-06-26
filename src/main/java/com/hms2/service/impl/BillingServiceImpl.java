package com.hms2.service.impl;

import com.hms2.model.Billing;
import com.hms2.model.Patient;
import com.hms2.model.Appointment;
import com.hms2.repository.BillingRepository;
import com.hms2.repository.PatientRepository;
import com.hms2.repository.AppointmentRepository;
import com.hms2.service.BillingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class BillingServiceImpl implements BillingService {
    
    private static final Logger logger = LoggerFactory.getLogger(BillingServiceImpl.class);
    
    @Inject
    private BillingRepository billingRepository;
    
    @Inject
    private PatientRepository patientRepository;
    
    @Inject
    private AppointmentRepository appointmentRepository;
    
    @Override
    public Billing createBilling(Billing billing) {
        try {
            if (billing.getInvoiceNumber() == null || billing.getInvoiceNumber().isEmpty()) {
                billing.setInvoiceNumber(generateInvoiceNumber());
            }
            
            if (billing.getBillingDate() == null) {
                billing.setBillingDate(LocalDateTime.now());
            }
            
            if (billing.getStatus() == null) {
                billing.setStatus("PENDING");
            }
            
            Billing savedBilling = billingRepository.save(billing);
            logger.info("Created billing with ID: {}", savedBilling.getBillingId());
            return savedBilling;
            
        } catch (Exception e) {
            logger.error("Error creating billing", e);
            throw new RuntimeException("Error creating billing: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Billing updateBilling(Billing billing) {
        try {
            Billing existingBilling = billingRepository.findById(billing.getBillingId())
                .orElseThrow(() -> new RuntimeException("Billing not found with ID: " + billing.getBillingId()));
            
            // Update fields
            existingBilling.setTotalAmount(billing.getTotalAmount());
            existingBilling.setPaidAmount(billing.getPaidAmount());
            existingBilling.setDiscountAmount(billing.getDiscountAmount());
            existingBilling.setTaxAmount(billing.getTaxAmount());
            existingBilling.setStatus(billing.getStatus());
            existingBilling.setPaymentMethod(billing.getPaymentMethod());
            existingBilling.setDueDate(billing.getDueDate());
            existingBilling.setDescription(billing.getDescription());
            existingBilling.setNotes(billing.getNotes());
            
            Billing updatedBilling = billingRepository.update(existingBilling);
            logger.info("Updated billing with ID: {}", updatedBilling.getBillingId());
            return updatedBilling;
            
        } catch (Exception e) {
            logger.error("Error updating billing", e);
            throw new RuntimeException("Error updating billing: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteBilling(Long billingId) {
        try {
            billingRepository.delete(billingId);
            logger.info("Deleted billing with ID: {}", billingId);
        } catch (Exception e) {
            logger.error("Error deleting billing", e);
            throw new RuntimeException("Error deleting billing: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Billing getBillingById(Long billingId) {
        return billingRepository.findById(billingId)
            .orElseThrow(() -> new RuntimeException("Billing not found with ID: " + billingId));
    }
    
    @Override
    public Billing getBillingByInvoiceNumber(String invoiceNumber) {
        return billingRepository.findByInvoiceNumber(invoiceNumber)
            .orElseThrow(() -> new RuntimeException("Billing not found with invoice number: " + invoiceNumber));
    }
    
    @Override
    public List<Billing> getAllBillings() {
        return billingRepository.findAll();
    }
    
    @Override
    public List<Billing> getBillingsByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));
        return billingRepository.findByPatient(patient);
    }
    
    @Override
    public List<Billing> getBillingsByAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));
        return billingRepository.findByAppointment(appointment);
    }
    
    @Override
    public List<Billing> getBillingsByStatus(String status) {
        return billingRepository.findByStatus(status);
    }
    
    @Override
    public List<Billing> getBillingsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return billingRepository.findByDateRange(startDate, endDate);
    }
    
    @Override
    public List<Billing> getPendingBills() {
        return billingRepository.findByStatus("PENDING");
    }
    
    @Override
    public List<Billing> getPendingBillsByPatient(Long patientId) {
        return getBillingsByPatient(patientId).stream()
            .filter(billing -> "PENDING".equals(billing.getStatus()) || "PARTIALLY_PAID".equals(billing.getStatus()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Billing> getOverdueBillings() {
        return billingRepository.findOverdueBillings();
    }
    
    @Override
    public void processPayment(Long billingId, BigDecimal amount, String paymentMethod) {
        try {
            Billing billing = getBillingById(billingId);
            billing.addPayment(amount, paymentMethod);
            billingRepository.update(billing);
            logger.info("Processed payment of {} for billing ID: {}", amount, billingId);
        } catch (Exception e) {
            logger.error("Error processing payment", e);
            throw new RuntimeException("Error processing payment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void markAsPaid(Long billingId) {
        try {
            Billing billing = getBillingById(billingId);
            billing.markAsPaid();
            billingRepository.update(billing);
            logger.info("Marked billing as paid: {}", billingId);
        } catch (Exception e) {
            logger.error("Error marking billing as paid", e);
            throw new RuntimeException("Error marking billing as paid: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void cancelBilling(Long billingId) {
        try {
            Billing billing = getBillingById(billingId);
            billing.cancel();
            billingRepository.update(billing);
            logger.info("Cancelled billing: {}", billingId);
        } catch (Exception e) {
            logger.error("Error cancelling billing", e);
            throw new RuntimeException("Error cancelling billing: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String generateInvoiceNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "INV-" + timestamp;
    }
    
    @Override
    public BigDecimal calculateTotalRevenue() {
        return getAllBillings().stream()
            .filter(billing -> "PAID".equals(billing.getStatus()))
            .map(Billing::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Override
    public BigDecimal calculatePendingAmount() {
        return getAllBillings().stream()
            .filter(billing -> !"PAID".equals(billing.getStatus()) && !"CANCELLED".equals(billing.getStatus()))
            .map(Billing::getBalanceAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Override
    public long getCountByStatus(String status) {
        return billingRepository.countByStatus(status);
    }
    
    @Override
    public List<Billing> getRecentBillings(int limit) {
        return getAllBillings().stream()
            .sorted((b1, b2) -> b2.getBillingDate().compareTo(b1.getBillingDate()))
            .limit(limit)
            .collect(Collectors.toList());
    }
}
