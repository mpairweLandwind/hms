package com.hms2.repository.impl;

import com.hms2.model.Prescription;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.enums.PrescriptionStatus;
import com.hms2.repository.PrescriptionRepository;
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
public class PrescriptionRepositoryImpl extends GenericRepositoryImpl<Prescription, Long>
        implements PrescriptionRepository {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionRepositoryImpl.class);

    @Override
    public List<Prescription> findByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                    "FROM Prescription p WHERE p.patient = :patient AND p.isDeleted = false ORDER BY p.prescriptionDate DESC",
                    Prescription.class);
            query.setParameter("patient", patient);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding prescriptions by patient: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding prescriptions by patient", e);
        }
    }

    @Override
    public List<Prescription> findByDoctor(Doctor doctor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                    "FROM Prescription p WHERE p.doctor = :doctor AND p.isDeleted = false ORDER BY p.prescriptionDate DESC",
                    Prescription.class);
            query.setParameter("doctor", doctor);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding prescriptions by doctor: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding prescriptions by doctor", e);
        }
    }

    @Override
    public List<Prescription> findByStatus(PrescriptionStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                    "FROM Prescription p WHERE p.status = :status AND p.isDeleted = false ORDER BY p.prescriptionDate DESC",
                    Prescription.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding prescriptions by status: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding prescriptions by status", e);
        }
    }

    @Override
    public List<Prescription> findByPatientAndStatus(Patient patient, PrescriptionStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                    "FROM Prescription p WHERE p.patient = :patient AND p.status = :status AND p.isDeleted = false ORDER BY p.prescriptionDate DESC",
                    Prescription.class);
            query.setParameter("patient", patient);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding prescriptions by patient and status: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding prescriptions by patient and status", e);
        }
    }

    @Override
    public List<Prescription> findByDoctorAndDateRange(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                    "FROM Prescription p WHERE p.doctor = :doctor AND p.prescriptionDate BETWEEN :startDate AND :endDate AND p.isDeleted = false ORDER BY p.prescriptionDate DESC",
                    Prescription.class);
            query.setParameter("doctor", doctor);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding prescriptions by doctor and date range: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding prescriptions by doctor and date range", e);
        }
    }

    @Override
    public List<Prescription> findActivePrescriptions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                    "FROM Prescription p WHERE p.status = :status AND p.isDeleted = false ORDER BY p.prescriptionDate DESC",
                    Prescription.class);
            query.setParameter("status", PrescriptionStatus.ACTIVE);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding active prescriptions: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding active prescriptions", e);
        }
    }

    @Override
    public List<Prescription> findExpiredPrescriptions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                    "FROM Prescription p WHERE p.expiryDate < :currentDate AND p.isDeleted = false ORDER BY p.prescriptionDate DESC",
                    Prescription.class);
            query.setParameter("currentDate", LocalDateTime.now());
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding expired prescriptions: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding expired prescriptions", e);
        }
    }

    @Override
    public List<Prescription> findRecentPrescriptions(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                    "FROM Prescription p WHERE p.isDeleted = false ORDER BY p.prescriptionDate DESC",
                    Prescription.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding recent prescriptions: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding recent prescriptions", e);
        }
    }

    @Override
    public List<Prescription> findDeletedPrescriptions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery(
                    "FROM Prescription p WHERE p.isDeleted = true", Prescription.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding deleted prescriptions: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding deleted prescriptions", e);
        }
    }

    @Override
    public long countByStatus(PrescriptionStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Prescription p WHERE p.status = :status AND p.isDeleted = false", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting prescriptions by status: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error counting prescriptions by status", e);
        }
    }

    @Override
    public long countByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Prescription p WHERE p.patient = :patient AND p.isDeleted = false", Long.class);
            query.setParameter("patient", patient);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting prescriptions by patient: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error counting prescriptions by patient", e);
        }
    }

    @Override
    public long countByDoctor(Doctor doctor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Prescription p WHERE p.doctor = :doctor AND p.isDeleted = false", Long.class);
            query.setParameter("doctor", doctor);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting prescriptions by doctor: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error counting prescriptions by doctor", e);
        }
    }

    @Override
    public boolean existsByPatientAndMedication(Patient patient, String medicationName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Prescription p JOIN p.prescriptionMedications pm JOIN pm.medication m WHERE p.patient = :patient AND m.brandName = :medicationName AND p.isDeleted = false",
                    Long.class);
            query.setParameter("patient", patient);
            query.setParameter("medicationName", medicationName);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            System.err.println("Error checking prescription existence by patient and medication: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error checking prescription existence by patient and medication", e);
        }
    }

    @Override
    public List<Prescription> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Prescription> query = session.createQuery("FROM Prescription p WHERE p.isDeleted = false", Prescription.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding all prescriptions: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding all prescriptions", e);
        }
    }

    @Override
    public List<Prescription> findByDoctorAndStatus(Doctor doctor, PrescriptionStatus status) {
        // Not implemented, return empty list
        return List.of();
    }
    @Override
    public List<Prescription> findByDoctorAndStatus(Doctor doctor, String status) {
        // Not implemented, return empty list
        return List.of();
    }
}
