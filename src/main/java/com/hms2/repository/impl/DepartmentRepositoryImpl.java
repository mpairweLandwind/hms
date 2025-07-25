package com.hms2.repository.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.model.Department;
import com.hms2.repository.DepartmentRepository;
import com.hms2.util.HibernateUtil;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentRepositoryImpl extends GenericRepositoryImpl<Department, Long>
        implements DepartmentRepository {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentRepositoryImpl.class);

    @Override
    public Optional<Department> findByDepartmentName(String departmentName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Department> query = session.createQuery(
                    "FROM Department d WHERE d.departmentName = :departmentName AND d.isDeleted = false", Department.class);
            query.setParameter("departmentName", departmentName);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding department by name", e);
            throw new RuntimeException("Error finding department by name", e);
        }
    }

    @Override
    public List<Department> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Department> query = session.createQuery(
                    "FROM Department d WHERE d.status = :status AND d.isDeleted = false", Department.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding departments by status", e);
            throw new RuntimeException("Error finding departments by status", e);
        }
    }

    @Override
    public List<Department> findByLocation(String location) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Department> query = session.createQuery(
                    "FROM Department d WHERE d.location = :location AND d.isDeleted = false", Department.class);
            query.setParameter("location", location);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding departments by location", e);
            throw new RuntimeException("Error finding departments by location", e);
        }
    }

    @Override
    public List<Department> searchByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Department> query = session.createQuery(
                    "FROM Department d WHERE LOWER(d.departmentName) LIKE LOWER(:name) AND d.isDeleted = false",
                    Department.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error searching departments by name", e);
            throw new RuntimeException("Error searching departments by name", e);
        }
    }

    @Override
    public List<Department> findDeletedDepartments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Department> query = session.createQuery(
                    "FROM Department d WHERE d.isDeleted = true", Department.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted departments", e);
            throw new RuntimeException("Error finding deleted departments", e);
        }
    }

    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Department d WHERE d.status = :status AND d.isDeleted = false", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting departments by status", e);
            throw new RuntimeException("Error counting departments by status", e);
        }
    }

    @Override
    public List<Department> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Department> query = session.createQuery("FROM Department d WHERE d.isDeleted = false", Department.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all departments", e);
            throw new RuntimeException("Error finding all departments", e);
        }
    }

    public void createDepartment(Department department) {
        logger.info("[REPO CREATE] Department received: {}", department);
        try {
            // ... existing code ...
            logger.info("[REPO SUCCESS] Department created: {}", department);
        } catch (Exception e) {
            logger.error("[REPO ERROR] Failed to create department: {}", department, e);
            throw e;
        }
    }

    public void updateDepartment(Department department) {
        logger.info("[REPO UPDATE] Department received: {}", department);
        try {
            // ... existing code ...
            logger.info("[REPO SUCCESS] Department updated: {}", department);
        } catch (Exception e) {
            logger.error("[REPO ERROR] Failed to update department: {}", department, e);
            throw e;
        }
    }
}
