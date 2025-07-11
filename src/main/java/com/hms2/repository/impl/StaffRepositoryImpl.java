package com.hms2.repository.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.enums.StaffStatus;
import com.hms2.model.Department;
import com.hms2.model.Staff;
import com.hms2.repository.StaffRepository;
import com.hms2.util.HibernateUtil;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StaffRepositoryImpl extends GenericRepositoryImpl<Staff, Long>
        implements StaffRepository {

    private static final Logger logger = LoggerFactory.getLogger(StaffRepositoryImpl.class);

    @Override
    public List<Staff> findByDepartment(Department department) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery(
                    "FROM Staff s WHERE s.department = :department AND s.isDeleted = false", Staff.class);
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
                    "FROM Staff s WHERE s.status = :status AND s.isDeleted = false", Staff.class);
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
                    "FROM Staff s WHERE s.active = :active AND s.isDeleted = false", Staff.class);
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
                    "FROM Staff s WHERE s.employeeId = :employeeId AND s.isDeleted = false", Staff.class);
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
                    "FROM Staff s WHERE s.email = :email AND s.isDeleted = false", Staff.class);
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
                    "FROM Staff s WHERE s.position = :position AND s.isDeleted = false", Staff.class);
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
                    "FROM Staff s WHERE LOWER(CONCAT(s.firstName, ' ', s.lastName)) LIKE LOWER(:name) AND s.isDeleted = false",
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
                    "SELECT COUNT(*) FROM Staff s WHERE s.department = :department AND s.isDeleted = false", Long.class);
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
                    "SELECT COUNT(*) FROM Staff s WHERE s.status = :status AND s.isDeleted = false", Long.class);
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
                    "FROM Staff s WHERE s.isDeleted = true", Staff.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted staff", e);
            throw new RuntimeException("Error finding deleted staff", e);
        }
    }

    @Override
    public List<Staff> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery("FROM Staff s WHERE s.isDeleted = false", Staff.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all staff", e);
            throw new RuntimeException("Error finding all staff", e);
        }
    }

    @Override
    public Optional<Staff> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery(
                "SELECT s FROM Staff s JOIN FETCH s.user WHERE s.id = :id AND s.isDeleted = false", Staff.class);
            query.setParameter("id", id);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding staff by ID with user fetch", e);
            throw new RuntimeException("Error finding staff by ID with user fetch", e);
        }
    }
}
