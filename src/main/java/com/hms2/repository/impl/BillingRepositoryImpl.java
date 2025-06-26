package com.hms2.repository.impl;

import com.hms2.model.Billing;
import com.hms2.model.Patient;
import com.hms2.model.Appointment;
import com.hms2.repository.BillingRepository;
import com.hms2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BillingRepositoryImpl extends GenericRepositoryImpl<Billing, Long> 
        implements BillingRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(BillingRepositoryImpl.class);
    
    @Override
    public Optional<Billing> findByInvoiceNumber(String invoiceNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                "FROM Billing b WHERE b.invoiceNumber = :invoiceNumber AND b.deleted = false", 
                Billing.class);
            query.setParameter("invoiceNumber", invoiceNumber);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding billing by invoice number", e);
            throw new RuntimeException("Error finding billing by invoice number", e);
        }
    }
    
    @Override
    public List<Billing> findByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                "FROM Billing b WHERE b.patient = :patient AND b.deleted = false ORDER BY b.billingDate DESC", 
                Billing.class);
            query.setParameter("patient", patient);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding billings by patient", e);
            throw new RuntimeException("Error finding billings by patient", e);
        }
    }
    
    @Override
    public List<Billing> findByAppointment(Appointment appointment) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                "FROM Billing b WHERE b.appointment = :appointment AND b.deleted = false", 
                Billing.class);
            query.setParameter("appointment", appointment);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding billings by appointment", e);
            throw new RuntimeException("Error finding billings by appointment", e);
        }
    }
    
    @Override
    public List<Billing> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                "FROM Billing b WHERE b.status = :status AND b.deleted = false", 
                Billing.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding billings by status", e);
            throw new RuntimeException("Error finding billings by status", e);
        }
    }
    
    @Override
    public List<Billing> findByPaymentMethod(String paymentMethod) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                "FROM Billing b WHERE b.paymentMethod = :paymentMethod AND b.deleted = false", 
                Billing.class);
            query.setParameter("paymentMethod", paymentMethod);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding billings by payment method", e);
            throw new RuntimeException("Error finding billings by payment method", e);
        }
    }
    
    @Override
    public List<Billing> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                "FROM Billing b WHERE b.billingDate BETWEEN :startDate AND :endDate AND b.deleted = false ORDER BY b.billingDate DESC", 
                Billing.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding billings by date range", e);
            throw new RuntimeException("Error finding billings by date range", e);
        }
    }
    
    @Override
    public List<Billing> findOverdueBillings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                "FROM Billing b WHERE (b.status = 'OVERDUE' OR (b.dueDate IS NOT NULL AND b.dueDate < :now AND b.status != 'PAID')) AND b.deleted = false", 
                Billing.class);
            query.setParameter("now", LocalDateTime.now());
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding overdue billings", e);
            throw new RuntimeException("Error finding overdue billings", e);
        }
    }
    
    @Override
    public List<Billing> findDeletedBillings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery(
                "FROM Billing b WHERE b.deleted = true", Billing.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted billings", e);
            throw new RuntimeException("Error finding deleted billings", e);
        }
    }
    
    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Billing b WHERE b.status = :status AND b.deleted = false", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting billings by status", e);
            throw new RuntimeException("Error counting billings by status", e);
        }
    }
    
    @Override
    public long countByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Billing b WHERE b.patient = :patient AND b.deleted = false", Long.class);
            query.setParameter("patient", patient);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting billings by patient", e);
            throw new RuntimeException("Error counting billings by patient", e);
        }
    }
    
    @Override
    public List<Billing> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Billing> query = session.createQuery("FROM Billing b WHERE b.deleted = false ORDER BY b.billingDate DESC", Billing.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all billings", e);
            throw new RuntimeException("Error finding all billings", e);
        }
    }
}
