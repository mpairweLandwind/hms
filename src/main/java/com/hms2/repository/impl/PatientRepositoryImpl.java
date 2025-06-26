package com.hms2.repository.impl;

import com.hms2.model.Patient;
import com.hms2.repository.PatientRepository;
import com.hms2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PatientRepositoryImpl extends GenericRepositoryImpl<Patient, Long> 
        implements PatientRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(PatientRepositoryImpl.class);
    
    @Override
    public Optional<Patient> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                "FROM Patient p WHERE p.email = :email AND p.deleted = false", Patient.class);
            query.setParameter("email", email);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding patient by email", e);
            throw new RuntimeException("Error finding patient by email", e);
        }
    }
    
    @Override
    public List<Patient> findByActive(boolean active) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                "FROM Patient p WHERE p.active = :active AND p.deleted = false", Patient.class);
            query.setParameter("active", active);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding patients by active status", e);
            throw new RuntimeException("Error finding patients by active status", e);
        }
    }
    
    @Override
    public List<Patient> searchByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                "FROM Patient p WHERE LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(:name) AND p.deleted = false", 
                Patient.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error searching patients by name", e);
            throw new RuntimeException("Error searching patients by name", e);
        }
    }
    
    @Override
    public List<Patient> findByBloodType(String bloodType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                "FROM Patient p WHERE p.bloodType = :bloodType AND p.deleted = false", Patient.class);
            query.setParameter("bloodType", bloodType);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding patients by blood type", e);
            throw new RuntimeException("Error finding patients by blood type", e);
        }
    }
    
    @Override
    public List<Patient> findByGender(String gender) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                "FROM Patient p WHERE p.gender = :gender AND p.deleted = false", Patient.class);
            query.setParameter("gender", gender);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding patients by gender", e);
            throw new RuntimeException("Error finding patients by gender", e);
        }
    }
    
    @Override
    public List<Patient> findDeletedPatients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery(
                "FROM Patient p WHERE p.deleted = true", Patient.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted patients", e);
            throw new RuntimeException("Error finding deleted patients", e);
        }
    }
    
    @Override
    public long countByActive(boolean active) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Patient p WHERE p.active = :active AND p.deleted = false", Long.class);
            query.setParameter("active", active);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting patients by active status", e);
            throw new RuntimeException("Error counting patients by active status", e);
        }
    }
    
    @Override
    public long countByGender(String gender) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Patient p WHERE p.gender = :gender AND p.deleted = false", Long.class);
            query.setParameter("gender", gender);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting patients by gender", e);
            throw new RuntimeException("Error counting patients by gender", e);
        }
    }
    
    @Override
    public List<Patient> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Patient> query = session.createQuery("FROM Patient p WHERE p.deleted = false", Patient.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all patients", e);
            throw new RuntimeException("Error finding all patients", e);
        }
    }
}
