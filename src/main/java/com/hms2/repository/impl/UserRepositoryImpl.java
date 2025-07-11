package com.hms2.repository.impl;

import com.hms2.model.User;
import com.hms2.repository.UserRepository;
import com.hms2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepositoryImpl extends GenericRepositoryImpl<User, Long> 
        implements UserRepository {
    
    @Override
    public Optional<User> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.username = :username AND u.isDeleted = false", User.class);
            query.setParameter("username", username);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            System.err.println("Error finding user by username");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding user by username", e);
        }
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.email = :email AND u.isDeleted = false", User.class);
            query.setParameter("email", email);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            System.err.println("Error finding user by email");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding user by email", e);
        }
    }
    
    @Override
    public List<User> findByRole(String role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.role = :role AND u.isDeleted = false", User.class);
            query.setParameter("role", role);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding users by role");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding users by role", e);
        }
    }
    
    @Override
    public List<User> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.status = :status AND u.isDeleted = false", User.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding users by status");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding users by status", e);
        }
    }
    
    @Override
    public List<User> findActiveUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.status = 'ACTIVE' AND u.isDeleted = false", User.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding active users");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding active users", e);
        }
    }
    
    @Override
    public List<User> findDeletedUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.isDeleted = true", User.class);
            List<User> deletedUsers = query.getResultList();
            System.err.println("[REPO] findDeletedUsers returned " + deletedUsers.size() + " deleted users");
            return deletedUsers;
        } catch (Exception e) {
            System.err.println("[ERROR] Error finding deleted users");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding deleted users", e);
        }
    }
    
    @Override
    public boolean existsByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM User u WHERE u.username = :username AND u.isDeleted = false", Long.class);
            query.setParameter("username", username);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            System.err.println("Error checking username existence");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error checking username existence", e);
        }
    }
    
    @Override
    public boolean existsByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM User u WHERE u.email = :email AND u.isDeleted = false", Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            System.err.println("Error checking email existence");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error checking email existence", e);
        }
    }
    
    @Override
    public long countByRole(String role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM User u WHERE u.role = :role AND u.isDeleted = false", Long.class);
            query.setParameter("role", role);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting users by role");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error counting users by role", e);
        }
    }
    
    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM User u WHERE u.status = :status AND u.isDeleted = false", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting users by status");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error counting users by status", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        System.err.println("[REPO] findById called for user ID: " + id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                System.err.println("[SUCCESS] User found by ID: " + user);
            } else {
                System.err.println("[WARNING] User not found by ID: " + id);
            }
            return Optional.ofNullable(user);
        } catch (Exception e) {
            System.err.println("[ERROR] Error finding user by ID: " + id);
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding user by ID", e);
        }
    }
    
    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User u WHERE u.isDeleted = false", User.class);
            List<User> users = query.getResultList();
            System.err.println("[REPO] findAll returned " + users.size() + " users");
            return users;
        } catch (Exception e) {
            System.err.println("[ERROR] Error finding all users");
            e.printStackTrace(System.err);
            throw new RuntimeException("Error finding all users", e);
        }
    }

    @Override
    public User update(User user) {
        System.err.println("[REPO] update called for user: " + user);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            System.err.println("[REPO] Merging user for update: " + user.getId());
            User updatedUser = session.merge(user);
            session.getTransaction().commit();
            System.err.println("[REPO][SUCCESS] User updated in repository: " + updatedUser);
            return updatedUser;
        } catch (Exception e) {
            System.err.println("[REPO][ERROR] Failed to update user in repository: " + user.getId() + ", reason: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error updating user", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        System.err.println("[REPO] deleteById called for user ID: " + id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                System.err.println("[REPO] Removing user from DB: " + id);
                session.remove(user);
                System.err.println("[REPO][SUCCESS] User permanently deleted from repository: " + id);
            } else {
                System.err.println("[REPO][WARNING] User not found for permanent deletion: " + id);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("[REPO][ERROR] Failed to permanently delete user: " + id + ", reason: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error permanently deleting user", e);
        }
    }
}
