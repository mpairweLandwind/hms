package com.hms2.repository.impl;

import com.hms2.model.Prescription;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.model.MedicalRecord;
import com.hms2.repository.PrescriptionRepository;
import com.hms2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PrescriptionRepositoryImpl extends GenericRepositoryImpl<Prescription, Long> 
        implements PrescriptionRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionRepositoryImpl.class);
    
    @Override
    public List<Prescription> findByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                "FROM Prescription p WHERE p.patient = :patient AND p.deleted = false ORDER BY p.prescriptionDate DESC", 
                Prescription.class);
            query.setParameter("patient", patient);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding prescriptions by patient", e);
            throw new RuntimeException("Error finding prescriptions by patient", e);
        }
    }
    
    @Override
    public List<Prescription> findByDoctor(Doctor doctor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                "FROM Prescription p WHERE p.doctor = :doctor AND p.deleted = false ORDER BY p.prescriptionDate DESC", 
                Prescription.class);
            query.setParameter("doctor", doctor);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding prescriptions by doctor", e);
            throw new RuntimeException("Error finding prescriptions by doctor", e);
        }
    }
    
    @Override
    public List<Prescription> findByMedicalRecord(MedicalRecord medicalRecord) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                "FROM Prescription p WHERE p.medicalRecord = :medicalRecord AND p.deleted = false", 
                Prescription.class);
            query.setParameter("medicalRecord", medicalRecord);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding prescriptions by medical record", e);
            throw new RuntimeException("Error finding prescriptions by medical record", e);
        }
    }
    
    @Override
    public List<Prescription> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                "FROM Prescription p WHERE p.status = :status AND p.deleted = false", 
                Prescription.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding prescriptions by status", e);
            throw new RuntimeException("Error finding prescriptions by status", e);
        }
    }
    
    @Override
    public List<Prescription> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                "FROM Prescription p WHERE p.prescriptionDate BETWEEN :startDate AND :endDate AND p.deleted = false ORDER BY p.prescriptionDate DESC", 
                Prescription.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding prescriptions by date range", e);
            throw new RuntimeException("Error finding prescriptions by date range", e);
        }
    }
    
    @Override
    public List<Prescription> findActivePrescriptions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                "FROM Prescription p WHERE p.status = 'ACTIVE' AND (p.expiryDate IS NULL OR p.expiryDate > :now) AND p.deleted = false", 
                Prescription.class);
            query.setParameter("now", LocalDateTime.now());
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding active prescriptions", e);
            throw new RuntimeException("Error finding active prescriptions", e);
        }
    }
    
    @Override
    public List<Prescription> findExpiredPrescriptions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                "FROM Prescription p WHERE (p.status = 'EXPIRED' OR (p.expiryDate IS NOT NULL AND p.expiryDate <= :now)) AND p.deleted = false", 
                Prescription.class);
            query.setParameter("now", LocalDateTime.now());
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding expired prescriptions", e);
            throw new RuntimeException("Error finding expired prescriptions", e);
        }
    }
    
    @Override
    public List<Prescription> findDeletedPrescriptions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                "FROM Prescription p WHERE p.deleted = true", Prescription.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted prescriptions", e);
            throw new RuntimeException("Error finding deleted prescriptions", e);
        }
    }
    
    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Prescription p WHERE p.status = :status AND p.deleted = false", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting prescriptions by status", e);
            throw new RuntimeException("Error counting prescriptions by status", e);
        }
    }
    
    @Override
    public long countByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Prescription p WHERE p.patient = :patient AND p.deleted = false", Long.class);
            query.setParameter("patient", patient);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting prescriptions by patient", e);
            throw new RuntimeException("Error counting prescriptions by patient", e);
        }
    }
    
    @Override
    public List<Prescription> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery("FROM Prescription p WHERE p.deleted = false ORDER BY p.prescriptionDate DESC", Prescription.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all prescriptions", e);
            throw new RuntimeException("Error finding all prescriptions", e);
        }
    }
}
