package com.hms2.repository.impl;

import com.hms2.model.Medication;
import com.hms2.repository.MedicationRepository;
import com.hms2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MedicationRepositoryImpl extends GenericRepositoryImpl<Medication, Long> 
        implements MedicationRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(MedicationRepositoryImpl.class);
    
    @Override
    public Optional<Medication> findByMedicationName(String medicationName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Medication> query = session.createQuery(
                "FROM Medication m WHERE m.medicationName = :medicationName AND m.deleted = false", 
                Medication.class);
            query.setParameter("medicationName", medicationName);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding medication by name", e);
            throw new RuntimeException("Error finding medication by name", e);
        }
    }
    
    @Override
    public List<Medication> findByGenericName(String genericName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Medication> query = session.createQuery(
                "FROM Medication m WHERE m.genericName = :genericName AND m.deleted = false", 
                Medication.class);
            query.setParameter("genericName", genericName);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding medications by generic name", e);
            throw new RuntimeException("Error finding medications by generic name", e);
        }
    }
    
    @Override
    public List<Medication> findByBrandName(String brandName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Medication> query = session.createQuery(
                "FROM Medication m WHERE m.brandName = :brandName AND m.deleted = false", 
                Medication.class);
            query.setParameter("brandName", brandName);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding medications by brand name", e);
            throw new RuntimeException("Error finding medications by brand name", e);
        }
    }
    
    @Override
    public List<Medication> findByManufacturer(String manufacturer) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Medication> query = session.createQuery(
                "FROM Medication m WHERE m.manufacturer = :manufacturer AND m.deleted = false", 
                Medication.class);
            query.setParameter("manufacturer", manufacturer);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding medications by manufacturer", e);
            throw new RuntimeException("Error finding medications by manufacturer", e);
        }
    }
    
    @Override
    public List<Medication> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Medication> query = session.createQuery(
                "FROM Medication m WHERE m.status = :status AND m.deleted = false", 
                Medication.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding medications by status", e);
            throw new RuntimeException("Error finding medications by status", e);
        }
    }
    
    @Override
    public List<Medication> findAvailableMedications() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Medication> query = session.createQuery(
                "FROM Medication m WHERE m.status = 'AVAILABLE' AND m.stockQuantity > 0 AND m.deleted = false", 
                Medication.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding available medications", e);
            throw new RuntimeException("Error finding available medications", e);
        }
    }
    
    @Override
    public List<Medication> findLowStockMedications() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Medication> query = session.createQuery(
                "FROM Medication m WHERE m.stockQuantity <= m.minimumStockLevel AND m.deleted = false", 
                Medication.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding low stock medications", e);
            throw new RuntimeException("Error finding low stock medications", e);
        }
    }
    
    @Override
    public List<Medication> findDeletedMedications() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Medication> query = session.createQuery(
                "FROM Medication m WHERE m.deleted = true", Medication.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted medications", e);
            throw new RuntimeException("Error finding deleted medications", e);
        }
    }
    
    @Override
    public List<Medication> searchByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Medication> query = session.createQuery(
                "FROM Medication m WHERE (LOWER(m.medicationName) LIKE LOWER(:name) OR " +
                "LOWER(m.genericName) LIKE LOWER(:name) OR LOWER(m.brandName) LIKE LOWER(:name)) AND m.deleted = false", 
                Medication.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error searching medications by name", e);
            throw new RuntimeException("Error searching medications by name", e);
        }
    }
    
    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Medication m WHERE m.status = :status AND m.deleted = false", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting medications by status", e);
            throw new RuntimeException("Error counting medications by status", e);
        }
    }
    
    @Override
    public List<Medication> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Medication> query = session.createQuery("FROM Medication m WHERE m.deleted = false", Medication.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all medications", e);
            throw new RuntimeException("Error finding all medications", e);
        }
    }
}
