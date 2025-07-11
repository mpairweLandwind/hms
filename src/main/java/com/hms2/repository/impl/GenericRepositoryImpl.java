package com.hms2.repository.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.repository.GenericRepository;
import com.hms2.util.HibernateUtil;

public abstract class GenericRepositoryImpl<T, ID extends Serializable> 
        implements GenericRepository<T, ID> {
    
    private static final Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);
    
    private final Class<T> entityClass;
    
    @SuppressWarnings("unchecked")
    public GenericRepositoryImpl() {
        // More reliable way to get the entity class
        Class<?> clazz = getClass();
        while (clazz != null && !(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
            clazz = clazz.getSuperclass();
        }
        
        if (clazz != null && clazz.getGenericSuperclass() instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
            this.entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        } else {
            throw new IllegalStateException("Could not determine entity class for " + getClass().getName());
        }
    }
    
    @Override
    public T save(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            logger.debug("Entity saved: {}", entity);
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving entity", e);
            throw new RuntimeException("Error saving entity", e);
        }
    }
    
    @Override
    public T update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T updatedEntity = session.merge(entity);
            transaction.commit();
            System.err.println("Entity updated: " + updatedEntity);
            return updatedEntity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("[ERROR] Error updating entity: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Error updating entity", e);
        }
    }
    
    @Override
    public void delete(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
            logger.debug("Entity deleted: {}", entity);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting entity", e);
            throw new RuntimeException("Error deleting entity", e);
        }
    }
    
    @Override
    public void deleteById(ID id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }
            transaction.commit();
            logger.debug("Entity deleted by ID: {}", id);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting entity by ID", e);
            throw new RuntimeException("Error deleting entity by ID", e);
        }
    }


    @Override
    public Optional<T> findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            T entity = session.get(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            logger.error("Error finding entity by ID", e);
            throw new RuntimeException("Error finding entity by ID", e);
        }
    }
    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery("FROM " + entityClass.getSimpleName(), entityClass);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all entities", e);
            throw new RuntimeException("Error finding all entities", e);
        }
    }
    
    @Override
    public List<T> findAll(int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery("FROM " + entityClass.getSimpleName(), entityClass);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding entities with pagination", e);
            throw new RuntimeException("Error finding entities with pagination", e);
        }
    }
    
    @Override
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM " + entityClass.getSimpleName(), Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting entities", e);
            throw new RuntimeException("Error counting entities", e);
        }
    }
    
    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }
}
