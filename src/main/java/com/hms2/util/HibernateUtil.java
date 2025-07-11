package com.hms2.util;

import com.hms2.model.*;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class HibernateUtil {
    

    private static final SessionFactory sessionFactory;
    
    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            
            // Explicitly add all annotated model classes
            configuration.addAnnotatedClass(com.hms2.model.User.class);
            configuration.addAnnotatedClass(com.hms2.model.Patient.class);
            configuration.addAnnotatedClass(com.hms2.model.Doctor.class);
            configuration.addAnnotatedClass(com.hms2.model.Staff.class);
            configuration.addAnnotatedClass(com.hms2.model.Appointment.class);
            configuration.addAnnotatedClass(com.hms2.model.Billing.class);
            configuration.addAnnotatedClass(com.hms2.model.Department.class);
            configuration.addAnnotatedClass(com.hms2.model.MedicalRecord.class);
            configuration.addAnnotatedClass(com.hms2.model.Medication.class);
            configuration.addAnnotatedClass(com.hms2.model.Prescription.class);
            configuration.addAnnotatedClass(com.hms2.model.PrescriptionMedication.class);
            
            
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
           System.err.println("Hibernate SessionFactory created successfully");
            
        } catch (Exception e) {
            System.err.println("Error creating SessionFactory");
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {         
            // but as a fallback, you could try re-initializing or throw a specific error.           
            System.err.println("SessionFactory is null. Re-check initialization.");
         
        }
        return sessionFactory;
    }
    
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("SessionFactory closed");
        }
    }
}
