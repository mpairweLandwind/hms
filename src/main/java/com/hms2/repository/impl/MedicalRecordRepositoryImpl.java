package com.hms2.repository.impl;

import com.hms2.model.MedicalRecord;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.model.Appointment;
import com.hms2.repository.MedicalRecordRepository;
import com.hms2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class MedicalRecordRepositoryImpl extends GenericRepositoryImpl<MedicalRecord, Long> 
        implements MedicalRecordRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordRepositoryImpl.class);
    
    @Override
    public List<MedicalRecord> findByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MedicalRecord> query = session.createQuery(
                "FROM MedicalRecord mr WHERE mr.patient = :patient AND mr.deleted = false ORDER BY mr.visitDate DESC", 
                MedicalRecord.class);
            query.setParameter("patient", patient);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding medical records by patient", e);
            throw new RuntimeException("Error finding medical records by patient", e);
        }
    }
    
    @Override
    public List<MedicalRecord> findByDoctor(Doctor doctor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MedicalRecord> query = session.createQuery(
                "FROM MedicalRecord mr WHERE mr.doctor = :doctor AND mr.deleted = false ORDER BY mr.visitDate DESC", 
                MedicalRecord.class);
            query.setParameter("doctor", doctor);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding medical records by doctor", e);
            throw new RuntimeException("Error finding medical records by doctor", e);
        }
    }
    
    @Override
    public List<MedicalRecord> findByAppointment(Appointment appointment) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MedicalRecord> query = session.createQuery(
                "FROM MedicalRecord mr WHERE mr.appointment = :appointment AND mr.deleted = false", 
                MedicalRecord.class);
            query.setParameter("appointment", appointment);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding medical records by appointment", e);
            throw new RuntimeException("Error finding medical records by appointment", e);
        }
    }
    
    @Override
    public List<MedicalRecord> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MedicalRecord> query = session.createQuery(
                "FROM MedicalRecord mr WHERE mr.status = :status AND mr.deleted = false", 
                MedicalRecord.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding medical records by status", e);
            throw new RuntimeException("Error finding medical records by status", e);
        }
    }
    
    @Override
    public List<MedicalRecord> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MedicalRecord> query = session.createQuery(
                "FROM MedicalRecord mr WHERE mr.visitDate BETWEEN :startDate AND :endDate AND mr.deleted = false ORDER BY mr.visitDate DESC", 
                MedicalRecord.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding medical records by date range", e);
            throw new RuntimeException("Error finding medical records by date range", e);
        }
    }
    
    @Override
    public List<MedicalRecord> findByDiagnosis(String diagnosis) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MedicalRecord> query = session.createQuery(
                "FROM MedicalRecord mr WHERE LOWER(mr.diagnosis) LIKE LOWER(:diagnosis) AND mr.deleted = false", 
                MedicalRecord.class);
            query.setParameter("diagnosis", "%" + diagnosis + "%");
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding medical records by diagnosis", e);
            throw new RuntimeException("Error finding medical records by diagnosis", e);
        }
    }
    
    @Override
    public List<MedicalRecord> findDeletedRecords() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MedicalRecord> query = session.createQuery(
                "FROM MedicalRecord mr WHERE mr.deleted = true", MedicalRecord.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted medical records", e);
            throw new RuntimeException("Error finding deleted medical records", e);
        }
    }
    
    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM MedicalRecord mr WHERE mr.status = :status AND mr.deleted = false", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting medical records by status", e);
            throw new RuntimeException("Error counting medical records by status", e);
        }
    }
    
    @Override
    public long countByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM MedicalRecord mr WHERE mr.patient = :patient AND mr.deleted = false", Long.class);
            query.setParameter("patient", patient);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting medical records by patient", e);
            throw new RuntimeException("Error counting medical records by patient", e);
        }
    }
    
    @Override
    public List<MedicalRecord> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MedicalRecord> query = session.createQuery("FROM MedicalRecord mr WHERE mr.deleted = false ORDER BY mr.visitDate DESC", MedicalRecord.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all medical records", e);
            throw new RuntimeException("Error finding all medical records", e);
        }
    }
}
