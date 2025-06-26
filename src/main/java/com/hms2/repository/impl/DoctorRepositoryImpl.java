package com.hms2.repository.impl;

import com.hms2.model.Doctor;
import com.hms2.model.Department;
import com.hms2.repository.DoctorRepository;
import com.hms2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DoctorRepositoryImpl extends GenericRepositoryImpl<Doctor, Long> 
        implements DoctorRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(DoctorRepositoryImpl.class);
    
    @Override
    public Optional<Doctor> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                "FROM Doctor d WHERE d.email = :email AND d.deleted = false", Doctor.class);
            query.setParameter("email", email);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding doctor by email", e);
            throw new RuntimeException("Error finding doctor by email", e);
        }
    }
    
    @Override
    public Optional<Doctor> findByLicenseNumber(String licenseNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                "FROM Doctor d WHERE d.licenseNumber = :licenseNumber AND d.deleted = false", Doctor.class);
            query.setParameter("licenseNumber", licenseNumber);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding doctor by license number", e);
            throw new RuntimeException("Error finding doctor by license number", e);
        }
    }
    
    @Override
    public List<Doctor> findByDepartment(Department department) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                "FROM Doctor d WHERE d.department = :department AND d.deleted = false", Doctor.class);
            query.setParameter("department", department);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding doctors by department", e);
            throw new RuntimeException("Error finding doctors by department", e);
        }
    }
    
    @Override
    public List<Doctor> findBySpecialization(String specialization) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                "FROM Doctor d WHERE d.specialization = :specialization AND d.deleted = false", Doctor.class);
            query.setParameter("specialization", specialization);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding doctors by specialization", e);
            throw new RuntimeException("Error finding doctors by specialization", e);
        }
    }
    
    @Override
    public List<Doctor> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                "FROM Doctor d WHERE d.status = :status AND d.deleted = false", Doctor.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding doctors by status", e);
            throw new RuntimeException("Error finding doctors by status", e);
        }
    }
    
    @Override
    public List<Doctor> findByActive(boolean active) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                "FROM Doctor d WHERE d.active = :active AND d.deleted = false", Doctor.class);
            query.setParameter("active", active);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding doctors by active status", e);
            throw new RuntimeException("Error finding doctors by active status", e);
        }
    }
    
    @Override
    public List<Doctor> searchByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                "FROM Doctor d WHERE LOWER(CONCAT(d.firstName, ' ', d.lastName)) LIKE LOWER(:name) AND d.deleted = false", 
                Doctor.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error searching doctors by name", e);
            throw new RuntimeException("Error searching doctors by name", e);
        }
    }
    
    @Override
    public List<Doctor> findDeletedDoctors() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                "FROM Doctor d WHERE d.deleted = true", Doctor.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted doctors", e);
            throw new RuntimeException("Error finding deleted doctors", e);
        }
    }
    
    @Override
    public long countByDepartment(Department department) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Doctor d WHERE d.department = :department AND d.deleted = false", Long.class);
            query.setParameter("department", department);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting doctors by department", e);
            throw new RuntimeException("Error counting doctors by department", e);
        }
    }
    
    @Override
    public long countBySpecialization(String specialization) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Doctor d WHERE d.specialization = :specialization AND d.deleted = false", Long.class);
            query.setParameter("specialization", specialization);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting doctors by specialization", e);
            throw new RuntimeException("Error counting doctors by specialization", e);
        }
    }
    
    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Doctor d WHERE d.status = :status AND d.deleted = false", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting doctors by status", e);
            throw new RuntimeException("Error counting doctors by status", e);
        }
    }
    
    @Override
    public List<Doctor> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery("FROM Doctor d WHERE d.deleted = false", Doctor.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all doctors", e);
            throw new RuntimeException("Error finding all doctors", e);
        }
    }
}
