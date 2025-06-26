package com.hms2.repository.impl;

import com.hms2.model.Appointment;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.repository.AppointmentRepository;
import com.hms2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class AppointmentRepositoryImpl extends GenericRepositoryImpl<Appointment, Long> 
        implements AppointmentRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(AppointmentRepositoryImpl.class);
    
    @Override
    public List<Appointment> findByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                "FROM Appointment a WHERE a.patient = :patient AND a.deleted = false", Appointment.class);
            query.setParameter("patient", patient);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding appointments by patient", e);
            throw new RuntimeException("Error finding appointments by patient", e);
        }
    }
    
    @Override
    public List<Appointment> findByDoctor(Doctor doctor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                "FROM Appointment a WHERE a.doctor = :doctor AND a.deleted = false", Appointment.class);
            query.setParameter("doctor", doctor);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding appointments by doctor", e);
            throw new RuntimeException("Error finding appointments by doctor", e);
        }
    }
    
    @Override
    public List<Appointment> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                "FROM Appointment a WHERE a.status = :status AND a.deleted = false", Appointment.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding appointments by status", e);
            throw new RuntimeException("Error finding appointments by status", e);
        }
    }
    
    @Override
    public List<Appointment> findByAppointmentType(String appointmentType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                "FROM Appointment a WHERE a.appointmentType = :appointmentType AND a.deleted = false", Appointment.class);
            query.setParameter("appointmentType", appointmentType);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding appointments by type", e);
            throw new RuntimeException("Error finding appointments by type", e);
        }
    }
    
    @Override
    public List<Appointment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                "FROM Appointment a WHERE a.appointmentDateTime BETWEEN :startDate AND :endDate AND a.deleted = false", 
                Appointment.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding appointments by date range", e);
            throw new RuntimeException("Error finding appointments by date range", e);
        }
    }
    
    @Override
    public List<Appointment> findTodaysAppointments() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        return findByDateRange(startOfDay, endOfDay);
    }
    
    @Override
    public List<Appointment> findUpcomingAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                "FROM Appointment a WHERE a.appointmentDateTime > :now AND a.deleted = false ORDER BY a.appointmentDateTime", 
                Appointment.class);
            query.setParameter("now", LocalDateTime.now());
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding upcoming appointments", e);
            throw new RuntimeException("Error finding upcoming appointments", e);
        }
    }
    
    @Override
    public List<Appointment> findDeletedAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                "FROM Appointment a WHERE a.deleted = true", Appointment.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted appointments", e);
            throw new RuntimeException("Error finding deleted appointments", e);
        }
    }
    
    @Override
    public List<Appointment> findConflictingAppointments(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                "FROM Appointment a WHERE a.doctor = :doctor AND a.deleted = false AND " +
                "((a.appointmentDateTime <= :startTime AND :startTime < a.appointmentDateTime + INTERVAL a.durationMinutes MINUTE) OR " +
                "(a.appointmentDateTime < :endTime AND :endTime <= a.appointmentDateTime + INTERVAL a.durationMinutes MINUTE) OR " +
                "(:startTime <= a.appointmentDateTime AND a.appointmentDateTime < :endTime))", 
                Appointment.class);
            query.setParameter("doctor", doctor);
            query.setParameter("startTime", startTime);
            query.setParameter("endTime", endTime);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding conflicting appointments", e);
            throw new RuntimeException("Error finding conflicting appointments", e);
        }
    }
    
    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Appointment a WHERE a.status = :status AND a.deleted = false", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting appointments by status", e);
            throw new RuntimeException("Error counting appointments by status", e);
        }
    }
    
    @Override
    public long countByDoctor(Doctor doctor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Appointment a WHERE a.doctor = :doctor AND a.deleted = false", Long.class);
            query.setParameter("doctor", doctor);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting appointments by doctor", e);
            throw new RuntimeException("Error counting appointments by doctor", e);
        }
    }
    
    @Override
    public List<Appointment> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery("FROM Appointment a WHERE a.deleted = false", Appointment.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all appointments", e);
            throw new RuntimeException("Error finding all appointments", e);
        }
    }
}
