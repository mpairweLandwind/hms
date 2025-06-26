package com.hms2.repository.impl;

import com.hms2.model.User;
import com.hms2.repository.UserRepository;
import com.hms2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepositoryImpl extends GenericRepositoryImpl<User, Long> 
        implements UserRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    
    @Override
    public Optional<User> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.username = :username AND u.deleted = false", User.class);
            query.setParameter("username", username);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding user by username", e);
            throw new RuntimeException("Error finding user by username", e);
        }
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.email = :email AND u.deleted = false", User.class);
            query.setParameter("email", email);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding user by email", e);
            throw new RuntimeException("Error finding user by email", e);
        }
    }
    
    @Override
    public List<User> findByRole(String role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.role = :role AND u.deleted = false", User.class);
            query.setParameter("role", role);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding users by role", e);
            throw new RuntimeException("Error finding users by role", e);
        }
    }
    
    @Override
    public List<User> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.status = :status AND u.deleted = false", User.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding users by status", e);
            throw new RuntimeException("Error finding users by status", e);
        }
    }
    
    @Override
    public List<User> findActiveUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.status = 'ACTIVE' AND u.deleted = false", User.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding active users", e);
            throw new RuntimeException("Error finding active users", e);
        }
    }
    
    @Override
    public List<User> findDeletedUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.deleted = true", User.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted users", e);
            throw new RuntimeException("Error finding deleted users", e);
        }
    }
    
    @Override
    public boolean existsByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM User u WHERE u.username = :username AND u.deleted = false", Long.class);
            query.setParameter("username", username);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            logger.error("Error checking username existence", e);
            throw new RuntimeException("Error checking username existence", e);
        }
    }
    
    @Override
    public boolean existsByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM User u WHERE u.email = :email AND u.deleted = false", Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            logger.error("Error checking email existence", e);
            throw new RuntimeException("Error checking email existence", e);
        }
    }
    
    @Override
    public long countByRole(String role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM User u WHERE u.role = :role AND u.deleted = false", Long.class);
            query.setParameter("role", role);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting users by role", e);
            throw new RuntimeException("Error counting users by role", e);
        }
    }
    
    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM User u WHERE u.status = :status AND u.deleted = false", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting users by status", e);
            throw new RuntimeException("Error counting users by status", e);
        }
    }
    
    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User u WHERE u.deleted = false", User.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all users", e);
            throw new RuntimeException("Error finding all users", e);
        }
    }
}
