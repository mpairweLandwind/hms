package com.hms2.repository.impl;

import com.hms2.model.Staff;
import com.hms2.model.StaffStatus;
import com.hms2.model.Department;
import com.hms2.repository.StaffRepository;
import com.hms2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StaffRepositoryImpl extends GenericRepositoryImpl<Staff, Long> 
        implements StaffRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(StaffRepositoryImpl.class);
    
    @Override
    public List<Staff> findByDepartment(Department department) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery(
                "FROM Staff s WHERE s.department = :department", Staff.class);
            query.setParameter("department", department);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding staff by department", e);
            throw new RuntimeException("Error finding staff by department", e);
        }
    }
    
    @Override
    public List<Staff> findByStatus(StaffStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery(
                "FROM Staff s WHERE s.status = :status", Staff.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding staff by status", e);
            throw new RuntimeException("Error finding staff by status", e);
        }
    }
    
    @Override
    public List<Staff> findByActive(boolean active) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery(
                "FROM Staff s WHERE s.active = :active", Staff.class);
            query.setParameter("active", active);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding staff by active status", e);
            throw new RuntimeException("Error finding staff by active status", e);
        }
    }
    
    @Override
    public Optional<Staff> findByEmployeeId(String employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery(
                "FROM Staff s WHERE s.employeeId = :employeeId", Staff.class);
            query.setParameter("employeeId", employeeId);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding staff by employee ID", e);
            throw new RuntimeException("Error finding staff by employee ID", e);
        }
    }
    
    @Override
    public Optional<Staff> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery(
                "FROM Staff s WHERE s.email = :email", Staff.class);
            query.setParameter("email", email);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding staff by email", e);
            throw new RuntimeException("Error finding staff by email", e);
        }
    }
    
    @Override
    public List<Staff> findByPosition(String position) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery(
                "FROM Staff s WHERE s.position = :position", Staff.class);
            query.setParameter("position", position);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding staff by position", e);
            throw new RuntimeException("Error finding staff by position", e);
        }
    }
    
    @Override
    public List<Staff> searchByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery(
                "FROM Staff s WHERE LOWER(CONCAT(s.firstName, ' ', s.lastName)) LIKE LOWER(:name)", 
                Staff.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error searching staff by name", e);
            throw new RuntimeException("Error searching staff by name", e);
        }
    }
    
    @Override
    public long countByDepartment(Department department) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Staff s WHERE s.department = :department", Long.class);
            query.setParameter("department", department);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting staff by department", e);
            throw new RuntimeException("Error counting staff by department", e);
        }
    }
    
    @Override
    public long countByStatus(StaffStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Staff s WHERE s.status = :status", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting staff by status", e);
            throw new RuntimeException("Error counting staff by status", e);
        }
    }

    @Override
    public List<Staff> findDeletedStaff() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery(
                "FROM Staff s WHERE s.deleted = true", Staff.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted staff", e);
            throw new RuntimeException("Error finding deleted staff", e);
        }
    }

    @Override
    public List<Staff> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery("FROM Staff s WHERE s.deleted = false", Staff.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all staff", e);
            throw new RuntimeException("Error finding all staff", e);
        }
    }
}
