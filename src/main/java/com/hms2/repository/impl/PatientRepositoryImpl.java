package com.hms2.repository.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.hms2.enums.BloodType;
import com.hms2.model.Patient;
import com.hms2.repository.PatientRepository;
import com.hms2.util.HibernateUtil;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PatientRepositoryImpl extends GenericRepositoryImpl<Patient, Long>
        implements PatientRepository {

    @Override
    public Optional<Patient> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                    "FROM Patient p WHERE p.email = :email AND p.isDeleted = false", Patient.class);
            query.setParameter("email", email);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            System.err.println("Error finding patient by email");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding patient by email", e);
        }
    }

    @Override
    public Optional<Patient> findByPatientId(String patientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                    "FROM Patient p WHERE p.patientId = :patientId AND p.isDeleted = false", Patient.class);
            query.setParameter("patientId", patientId);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            System.err.println("Error finding patient by patient ID");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding patient by patient ID", e);
        }
    }

    @Override
    public List<Patient> findByBloodType(BloodType bloodType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                    "FROM Patient p WHERE p.bloodType = :bloodType AND p.isDeleted = false", Patient.class);
            query.setParameter("bloodType", bloodType);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding patients by blood type");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding patients by blood type", e);
        }
    }

    @Override
    public List<Patient> findByStatus(String status) {
        // For backward compatibility, treat "ACTIVE" as true, others as false
        boolean isActive = "ACTIVE".equalsIgnoreCase(status);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                    "FROM Patient p WHERE p.active = :active AND p.isDeleted = false", Patient.class);
            query.setParameter("active", isActive);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding patients by status");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding patients by status", e);
        }
    }

    @Override
    public List<Patient> findByDateOfBirthRange(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                    "FROM Patient p WHERE p.dateOfBirth BETWEEN :startDate AND :endDate AND p.isDeleted = false", Patient.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding patients by date of birth range");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding patients by date of birth range", e);
        }
    }

    @Override
    public List<Patient> findByGender(String gender) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                    "FROM Patient p WHERE p.gender = :gender AND p.isDeleted = false", Patient.class);
            query.setParameter("gender", gender);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding patients by gender");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding patients by gender", e);
        }
    }

    @Override
    public List<Patient> searchByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                    "FROM Patient p WHERE LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(:name) AND p.isDeleted = false",
                    Patient.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching patients by name");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error searching patients by name", e);
        }
    }

    @Override
    public List<Patient> findActivePatients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                    "FROM Patient p WHERE p.active = true AND p.isDeleted = false", Patient.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding active patients");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding active patients", e);
        }
    }

    @Override
    public List<Patient> findDeletedPatients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                    "FROM Patient p WHERE p.isDeleted = true", Patient.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding deleted patients");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding deleted patients", e);
        }
    }

    @Override
    public long countByStatus(String status) {
        // For backward compatibility, treat "ACTIVE" as true, others as false
        boolean isActive = "ACTIVE".equalsIgnoreCase(status);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Patient p WHERE p.active = :active AND p.isDeleted = false", Long.class);
            query.setParameter("active", isActive);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting patients by status");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error counting patients by status", e);
        }
    }

    @Override
    public long countByBloodType(BloodType bloodType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Patient p WHERE p.bloodType = :bloodType AND p.isDeleted = false", Long.class);
            query.setParameter("bloodType", bloodType);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting patients by blood type");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error counting patients by blood type", e);
        }
    }

    @Override
    public long countByGender(String gender) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Patient p WHERE p.gender = :gender AND p.isDeleted = false", Long.class);
            query.setParameter("gender", gender);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting patients by gender");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error counting patients by gender", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Patient p WHERE p.email = :email AND p.isDeleted = false", Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            System.err.println("Error checking patient email existence");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error checking patient email existence", e);
        }
    }

    @Override
    public boolean existsByPatientId(String patientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Patient p WHERE p.patientId = :patientId AND p.isDeleted = false", Long.class);
            query.setParameter("patientId", patientId);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            System.err.println("Error checking patient ID existence");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error checking patient ID existence", e);
        }
    }

    @Override
    public List<Patient> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery("FROM Patient p WHERE p.isDeleted = false", Patient.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding all patients");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding all patients", e);
        }
    }
    
    @Override
    public Optional<Patient> findByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                    "FROM Patient p WHERE p.user.id = :userId AND p.isDeleted = false", Patient.class);
            query.setParameter("userId", userId);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            System.err.println("Error finding patient by user ID");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding patient by user ID", e);
        }
    }
}
