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
import java.util.Optional;

@ApplicationScoped
public class PrescriptionMedicationRepositoryImpl extends GenericRepositoryImpl<PrescriptionMedication, Long>
        implements PrescriptionMedicationRepository {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionMedicationRepositoryImpl.class);

    @Override
    public List<PrescriptionMedication> findByPrescription(Prescription prescription) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PrescriptionMedication> query = session.createQuery(
                    "FROM PrescriptionMedication pm WHERE pm.prescription = :prescription AND pm.isDeleted = false",
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
                    "FROM PrescriptionMedication pm WHERE pm.medication = :medication AND pm.isDeleted = false",
                    PrescriptionMedication.class);
            query.setParameter("medication", medication);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding prescription medications by medication", e);
            throw new RuntimeException("Error finding prescription medications by medication", e);
        }
    }

    @Override
    public Optional<PrescriptionMedication> findByPrescriptionAndMedication(Prescription prescription, Medication medication) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PrescriptionMedication> query = session.createQuery(
                    "FROM PrescriptionMedication pm WHERE pm.prescription = :prescription AND pm.medication = :medication AND pm.isDeleted = false",
                    PrescriptionMedication.class);
            query.setParameter("prescription", prescription);
            query.setParameter("medication", medication);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding prescription medication by prescription and medication", e);
            throw new RuntimeException("Error finding prescription medication by prescription and medication", e);
        }
    }

    @Override
    public List<PrescriptionMedication> findByDosage(String dosage) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PrescriptionMedication> query = session.createQuery(
                    "FROM PrescriptionMedication pm WHERE pm.dosage = :dosage AND pm.isDeleted = false",
                    PrescriptionMedication.class);
            query.setParameter("dosage", dosage);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding prescription medications by dosage", e);
            throw new RuntimeException("Error finding prescription medications by dosage", e);
        }
    }

    @Override
    public List<PrescriptionMedication> findByFrequency(String frequency) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PrescriptionMedication> query = session.createQuery(
                    "FROM PrescriptionMedication pm WHERE pm.frequency = :frequency AND pm.isDeleted = false",
                    PrescriptionMedication.class);
            query.setParameter("frequency", frequency);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding prescription medications by frequency", e);
            throw new RuntimeException("Error finding prescription medications by frequency", e);
        }
    }

    @Override
    public List<PrescriptionMedication> findActiveMedications() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PrescriptionMedication> query = session.createQuery(
                    "FROM PrescriptionMedication pm WHERE pm.isDeleted = false",
                    PrescriptionMedication.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding active prescription medications", e);
            throw new RuntimeException("Error finding active prescription medications", e);
        }
    }

    @Override
    public List<PrescriptionMedication> findDeletedMedications() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PrescriptionMedication> query = session.createQuery(
                    "FROM PrescriptionMedication pm WHERE pm.isDeleted = true",
                    PrescriptionMedication.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted prescription medications", e);
            throw new RuntimeException("Error finding deleted prescription medications", e);
        }
    }

    @Override
    public long countByPrescription(Prescription prescription) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM PrescriptionMedication pm WHERE pm.prescription = :prescription AND pm.isDeleted = false",
                    Long.class);
            query.setParameter("prescription", prescription);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting prescription medications by prescription", e);
            throw new RuntimeException("Error counting prescription medications by prescription", e);
        }
    }

    @Override
    public long countByMedication(Medication medication) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM PrescriptionMedication pm WHERE pm.medication = :medication AND pm.isDeleted = false",
                    Long.class);
            query.setParameter("medication", medication);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting prescription medications by medication", e);
            throw new RuntimeException("Error counting prescription medications by medication", e);
        }
    }

    @Override
    public boolean existsByPrescriptionAndMedication(Prescription prescription, Medication medication) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM PrescriptionMedication pm WHERE pm.prescription = :prescription AND pm.medication = :medication AND pm.isDeleted = false",
                    Long.class);
            query.setParameter("prescription", prescription);
            query.setParameter("medication", medication);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            logger.error("Error checking prescription medication existence", e);
            throw new RuntimeException("Error checking prescription medication existence", e);
        }
    }

    @Override
    public List<PrescriptionMedication> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PrescriptionMedication> query = session.createQuery("FROM PrescriptionMedication pm WHERE pm.isDeleted = false", PrescriptionMedication.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all prescription medications", e);
            throw new RuntimeException("Error finding all prescription medications", e);
        }
    }
}
