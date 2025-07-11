package com.hms2.repository;

import com.hms2.model.Billing;
import com.hms2.model.Patient;
import com.hms2.model.Appointment;
import com.hms2.enums.BillingStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BillingRepository extends GenericRepository<Billing, Long> {

    List<Billing> findByPatient(Patient patient);

    List<Billing> findByAppointment(Appointment appointment);

    List<Billing> findByStatus(BillingStatus status);

    List<Billing> findByPatientAndStatus(Patient patient, BillingStatus status);

    List<Billing> findByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);

    List<Billing> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<Billing> findPendingBills();

    List<Billing> findPaidBills();

    List<Billing> findOverdueBills();

    List<Billing> findDeletedBills();

    long countByStatus(BillingStatus status);

    long countByPatient(Patient patient);

    BigDecimal getTotalAmountByStatus(BillingStatus status);

    BigDecimal getTotalAmountByPatient(Patient patient);

    BigDecimal getTotalRevenue();

    boolean existsByAppointment(Appointment appointment);
}
