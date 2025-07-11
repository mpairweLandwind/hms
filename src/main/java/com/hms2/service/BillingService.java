package com.hms2.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.hms2.dto.billing.BillingRequestDTO;
import com.hms2.dto.billing.BillingResponseDTO;
import com.hms2.dto.billing.BillingItemDTO;
import com.hms2.dto.billing.PaymentDTO;
import com.hms2.dto.dashboard.ChartDataDTO;
import com.hms2.enums.BillingStatus;
import com.hms2.model.Appointment;
import com.hms2.model.Billing;
import com.hms2.model.Patient;

public interface BillingService {

    // Basic CRUD operations
    Billing save(Billing billing);
    Billing update(Billing billing);
    void delete(Long id);
    Billing findOne(Long id);
    List<Billing> findAll();

    // Additional methods for compatibility
    Billing createBilling(Billing billing);
    Billing updateBilling(Billing billing);
    void deleteBilling(Long billingId);
    Billing getBillingById(Long billingId);
    List<Billing> getAllBillings();
    Billing findById(Long billingId);

    // DTO-based operations
    BillingResponseDTO createBillingFromDTO(BillingRequestDTO billingRequestDTO);
    BillingResponseDTO updateBillingFromDTO(Long billingId, BillingRequestDTO billingRequestDTO);
    BillingResponseDTO getBillingResponseById(Long billingId);
    List<BillingResponseDTO> getAllBillingResponses();
    List<BillingResponseDTO> getBillingsByPatientId(Long patientId);



    // Search methods
    List<Billing> getBillingsByPatient(Patient patient);
    List<Billing> getBillingsByAppointment(Appointment appointment);
    List<Billing> getBillingsByStatus(BillingStatus status);
    List<Billing> getBillingsByPatientAndStatus(Patient patient, BillingStatus status);
    List<Billing> getBillingsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // Status-based methods
    List<Billing> getPendingBillings();
    List<Billing> getPaidBillings();
    List<Billing> getOverdueBillings();
    List<BillingResponseDTO> getPendingBillingResponses();
   // List<BillingResponseDTO> getPaidBillingResponses();
    List<BillingResponseDTO> getOverdueBillingResponses();



    // Status update methods
    void markAsPaid(Long billingId);
    void markAsOverdue(Long billingId);
    void cancelBilling(Long billingId);
    void processPayment(Long billingId, BigDecimal amount, String paymentMethod);
    void approveBilling(Long billingId, String approvedBy, String approvalNotes);
    void rejectBilling(Long billingId, String rejectedBy, String rejectionReason);
    void disputeBilling(Long billingId, String disputeReason);
    void resolveDispute(Long billingId, String resolvedBy, String resolutionNotes);


    // Payment management
   // PaymentDTO processPaymentTransaction(Long billingId, PaymentDTO paymentDTO);
    List<PaymentDTO> getPaymentHistory(Long billingId);
    //PaymentDTO getPaymentById(Long paymentId);
    void refundPayment(Long paymentId, BigDecimal refundAmount, String refundReason, String refundedBy);
    void cancelPayment(Long paymentId, String cancelledBy, String cancellationReason);

    // Billing items management
    BillingItemDTO addBillingItem(Long billingId, BillingItemDTO billingItemDTO);
    //BillingItemDTO updateBillingItem(Long billingItemId, BillingItemDTO billingItemDTO);
    //void removeBillingItem(Long billingItemId);
    List<BillingItemDTO> getBillingItems(Long billingId);
    BillingItemDTO getBillingItemById(Long billingItemId);

    // Insurance management
    void updateInsuranceInfo(Long billingId, String insuranceProvider, String insuranceNumber, 
                           String policyNumber, Date policyExpiryDate, Boolean insuranceCovered, 
                           BigDecimal insuranceAmount);
    void submitInsuranceClaim(Long billingId, String claimNumber, String claimNotes);
    void updateClaimStatus(Long billingId, String claimStatus, String claimNotes);

    // Statistics methods
    long countBillingsByStatus(BillingStatus status);
    long countBillingsByPatient(Patient patient);
    BigDecimal getTotalAmountByStatus(BillingStatus status);
    BigDecimal getTotalAmountByPatient(Patient patient);
    BigDecimal getTotalRevenue();
    BigDecimal getAverageBillAmount();
    
    // Enhanced statistics
    long countBillingsByStatus(String status);
    long countBillingsByPatientId(Long patientId);
    // long countBillingsByDoctorId(Long doctorId);
    // long countBillingsByDateRange(Date startDate, Date endDate);
    long countOverdueBillings();
    long countDisputedBillings();
    long countUrgentBillings();
    
    BigDecimal getTotalRevenueByDateRange(Date startDate, Date endDate);
    BigDecimal getTotalRevenueByPatientId(Long patientId);
    // BigDecimal getTotalRevenueByDoctorId(Long doctorId);
    BigDecimal getTotalRevenueByDepartment(String department);
    BigDecimal getTotalRevenueByBillingType(String billingType);
    BigDecimal getTotalOverdueAmount();
    BigDecimal getTotalPendingAmount();
    BigDecimal getTotalCollectedAmount();
    BigDecimal getAverageBillAmountByDateRange(Date startDate, Date endDate);
    BigDecimal getAverageBillAmountByPatientId(Long patientId);
    // BigDecimal getAverageBillAmountByDoctorId(Long doctorId);

    // Dashboard statistics
    List<ChartDataDTO> getRevenueChartData(Date startDate, Date endDate, String period);
    List<ChartDataDTO> getBillingStatusChartData();
    List<ChartDataDTO> getBillingTypeChartData();
    List<ChartDataDTO> getDepartmentRevenueChartData();
    List<ChartDataDTO> getPaymentMethodChartData();
    List<ChartDataDTO> getOverdueBillingChartData();
    List<ChartDataDTO> getDailyRevenueChartData(Date startDate, Date endDate);
    List<ChartDataDTO> getMonthlyRevenueChartData(int year);
    List<ChartDataDTO> getYearlyRevenueChartData(int startYear, int endYear);

    // Utility methods
    boolean existsByAppointment(Appointment appointment);
    BigDecimal calculateTotalAmount(Billing billing);
    BigDecimal calculateBalanceAmount(Long billingId);
    Boolean isBillingFullyPaid(Long billingId);
    Boolean isBillingOverdue(Long billingId);
    Integer getDaysOverdue(Long billingId);
    String generateBillingNumber();
    void sendBillingNotification(Long billingId, String notificationType);
    void sendPaymentReminder(Long billingId);
    void sendOverdueNotification(Long billingId);
    
    // Soft Delete operations
    List<Billing> getDeletedBillings();
    List<BillingResponseDTO> getDeletedBillingResponses();
    void restoreBilling(Long billingId);
    void permanentlyDeleteBilling(Long billingId);
    
    // Bulk operations
    void bulkApproveBillings(List<Long> billingIds, String approvedBy, String approvalNotes);
    void bulkRejectBillings(List<Long> billingIds, String rejectedBy, String rejectionReason);
    void bulkSendReminders(List<Long> billingIds);
    void bulkUpdateStatus(List<Long> billingIds, String status, String updatedBy);
    
    // Export and reporting
    byte[] exportBillingsToPDF(List<Long> billingIds);
    byte[] exportBillingsToExcel(List<Long> billingIds);
    String generateBillingReport(Date startDate, Date endDate, String reportType);
    
    // Validation methods
    Boolean validateBillingRequest(BillingRequestDTO billingRequestDTO);
    Boolean validatePaymentRequest(PaymentDTO paymentDTO);
    Boolean validateBillingItemRequest(BillingItemDTO billingItemDTO);
    List<String> getBillingValidationErrors(BillingRequestDTO billingRequestDTO);
    List<String> getPaymentValidationErrors(PaymentDTO paymentDTO);
}
