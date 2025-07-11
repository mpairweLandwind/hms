package com.hms2.repository.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.hms2.enums.BillingStatus;
import com.hms2.model.Appointment;
import com.hms2.model.Billing;
import com.hms2.model.Patient;
import com.hms2.repository.BillingRepository;
import com.hms2.util.HibernateUtil;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BillingRepositoryImpl extends GenericRepositoryImpl<Billing, Long>
        implements BillingRepository {

    @Override
    public List<Billing> findByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.patient = :patient AND b.isDeleted = false ORDER BY b.billingDate DESC",
                    Billing.class);
            query.setParameter("patient", patient);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding billing by patient: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding billing by patient", e);
        }
    }

    @Override
    public List<Billing> findByAppointment(Appointment appointment) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.appointment = :appointment AND b.isDeleted = false",
                    Billing.class);
            query.setParameter("appointment", appointment);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding billing by appointment: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding billing by appointment", e);
        }
    }

    @Override
    public List<Billing> findByStatus(BillingStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.status = :status AND b.isDeleted = false ORDER BY b.billingDate DESC",
                    Billing.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding billing by status: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding billing by status", e);
        }
    }

    @Override
    public List<Billing> findByPatientAndStatus(Patient patient, BillingStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.patient = :patient AND b.status = :status AND b.isDeleted = false ORDER BY b.billingDate DESC",
                    Billing.class);
            query.setParameter("patient", patient);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding billing by patient and status: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding billing by patient and status", e);
        }
    }

    @Override
    public List<Billing> findByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.totalAmount BETWEEN :minAmount AND :maxAmount AND b.isDeleted = false ORDER BY b.billingDate DESC",
                    Billing.class);
            query.setParameter("minAmount", minAmount);
            query.setParameter("maxAmount", maxAmount);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding billing by amount range: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding billing by amount range", e);
        }
    }

    @Override
    public List<Billing> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.billingDate BETWEEN :startDate AND :endDate AND b.isDeleted = false ORDER BY b.billingDate DESC",
                    Billing.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding billing by date range: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding billing by date range", e);
        }
    }

    @Override
    public List<Billing> findPendingBills() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.status = :status AND b.isDeleted = false ORDER BY b.billingDate DESC",
                    Billing.class);
            query.setParameter("status", BillingStatus.PENDING.name());
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding pending bills: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding pending bills", e);
        }
    }

    @Override
    public List<Billing> findPaidBills() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.status = :status AND b.isDeleted = false ORDER BY b.billingDate DESC",
                    Billing.class);
            query.setParameter("status", BillingStatus.PAID.name());
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding paid bills: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding paid bills", e);
        }
    }

    @Override
    public List<Billing> findOverdueBills() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.status = :status AND b.isDeleted = false ORDER BY b.billingDate DESC",
                    Billing.class);
            query.setParameter("status", BillingStatus.OVERDUE.name());
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding overdue bills: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding overdue bills", e);
        }
    }

    @Override
    public List<Billing> findDeletedBills() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.isDeleted = true", Billing.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding deleted bills: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding deleted bills", e);
        }
    }

    @Override
    public long countByStatus(BillingStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Billing b WHERE b.status = :status AND b.isDeleted = false", Long.class);
            query.setParameter("status", status.name());
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting billing by status: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error counting billing by status", e);
        }
    }

    @Override
    public long countByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Billing b WHERE b.patient = :patient AND b.isDeleted = false", Long.class);
            query.setParameter("patient", patient);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting billing by patient: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error counting billing by patient", e);
        }
    }

    @Override
    public BigDecimal getTotalAmountByStatus(BillingStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<BigDecimal> query = session.createQuery(
                    "SELECT COALESCE(SUM(b.totalAmount), 0) FROM Billing b WHERE b.status = :status AND b.isDeleted = false",
                    BigDecimal.class);
            query.setParameter("status", status.name());
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error getting total amount by status: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error getting total amount by status", e);
        }
    }

    @Override
    public BigDecimal getTotalAmountByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<BigDecimal> query = session.createQuery(
                    "SELECT COALESCE(SUM(b.totalAmount), 0) FROM Billing b WHERE b.patient = :patient AND b.isDeleted = false",
                    BigDecimal.class);
            query.setParameter("patient", patient);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error getting total amount by patient: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error getting total amount by patient", e);
        }
    }

    @Override
    public BigDecimal getTotalRevenue() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<BigDecimal> query = session.createQuery(
                    "SELECT COALESCE(SUM(b.totalAmount), 0) FROM Billing b WHERE b.status = :status AND b.isDeleted = false",
                    BigDecimal.class);
            query.setParameter("status", BillingStatus.PAID.name());
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error getting total revenue: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error getting total revenue", e);
        }
    }

    @Override
    public boolean existsByAppointment(Appointment appointment) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Billing b WHERE b.appointment = :appointment AND b.isDeleted = false",
                    Long.class);
            query.setParameter("appointment", appointment);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            System.err.println("Error checking billing existence by appointment: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error checking billing existence by appointment", e);
        }
    }

    @Override
    public List<Billing> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery("FROM Billing b WHERE b.isDeleted = false", Billing.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding all billing records: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding all billing records", e);
        }
    }

    // Helper to convert string to BillingStatus
    private BillingStatus toBillingStatus(Object status) {
        if (status instanceof BillingStatus) {
            return (BillingStatus) status;
        } else if (status instanceof String) {
            return BillingStatus.valueOf(((String) status).toUpperCase());
        } else {
            throw new IllegalArgumentException("Invalid status type: " + status);
        }
    }

    // Overload for findByStatus
    public List<Billing> findByStatus(String status) {
        return findByStatus(toBillingStatus(status));
    }

    // Overload for countByStatus
    public long countByStatus(String status) {
        return countByStatus(toBillingStatus(status));
    }

    // Overload for getTotalAmountByStatus
    public BigDecimal getTotalAmountByStatus(String status) {
        return getTotalAmountByStatus(toBillingStatus(status));
    }
}
