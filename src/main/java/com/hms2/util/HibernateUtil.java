package com.hms2.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;
    
    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            logger.info("Hibernate SessionFactory created successfully");
            
        } catch (Exception e) {
            logger.error("Error creating SessionFactory", e);
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
