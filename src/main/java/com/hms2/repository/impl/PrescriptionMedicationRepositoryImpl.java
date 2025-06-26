package com.hms2.repository.impl;

import com.hms2.model.PrescriptionMedication;
import com.hms2.model.Prescription;
import com.hms2.model.Medication;
import com.hms2.repository.PrescriptionMedicationRepository;
import com.hms2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PrescriptionMedicationRepositoryImpl 
        extends GenericRepositoryImpl<PrescriptionMedication, Long> 
        implements PrescriptionMedicationRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionMedicationRepositoryImpl.class);
    
    @Override
    public List<PrescriptionMedication> findByPrescription(Prescription prescription) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PrescriptionMedication> query = session.createQuery(
                "FROM PrescriptionMedication pm WHERE pm.prescription = :prescription", 
                PrescriptionMedication.class);
            query.setParameter("prescription", prescription);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding prescription medications by prescription", e);
            throw new RuntimeException("Error finding prescription medications by prescription", e);
        }
    }
    
    @Override
    public List<PrescriptionMedication> findByMedication(Medication medication) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PrescriptionMedication> query = session.createQuery(
                "FROM PrescriptionMedication pm WHERE pm.medication = :medication", 
                PrescriptionMedication.class);
            query.setParameter("medication", medication);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding prescription medications by medication", e);
            throw new RuntimeException("Error finding prescription medications by medication", e);
        }
    }
    
    @Override
    public List<PrescriptionMedication> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PrescriptionMedication> query = session.createQuery(
                "FROM PrescriptionMedication pm WHERE pm.status = :status", 
                PrescriptionMedication.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding prescription medications by status", e);
            throw new RuntimeException("Error finding prescription medications by status", e);
        }
    }
    
    @Override
    public List<PrescriptionMedication> findPendingDispensals() {
        return findByStatus("PENDING");
    }
    
    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM PrescriptionMedication pm WHERE pm.status = :status", 
                Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting prescription medications by status", e);
            throw new RuntimeException("Error counting prescription medications by status", e);
        }
    }
}
