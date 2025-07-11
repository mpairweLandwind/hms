package com.hms2.repository.impl;

import com.hms2.model.Appointment;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.enums.AppointmentStatus;
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
import java.util.Optional;

@ApplicationScoped
public class AppointmentRepositoryImpl extends GenericRepositoryImpl<Appointment, Long>
        implements AppointmentRepository {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentRepositoryImpl.class);

    @Override
    public List<Appointment> findByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.patient = :patient AND a.isDeleted = false ORDER BY a.appointmentDateTime DESC",
                    Appointment.class);
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
                    "FROM Appointment a WHERE a.doctor = :doctor AND a.isDeleted = false ORDER BY a.appointmentDateTime DESC",
                    Appointment.class);
            query.setParameter("doctor", doctor);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding appointments by doctor", e);
            throw new RuntimeException("Error finding appointments by doctor", e);
        }
    }

    @Override
    public List<Appointment> findByStatus(AppointmentStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.status = :status AND a.isDeleted = false ORDER BY a.appointmentDateTime DESC",
                    Appointment.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding appointments by status", e);
            throw new RuntimeException("Error finding appointments by status", e);
        }
    }

    @Override
    public List<Appointment> findByPatientAndStatus(Patient patient, AppointmentStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.patient = :patient AND a.status = :status AND a.isDeleted = false ORDER BY a.appointmentDateTime DESC",
                    Appointment.class);
            query.setParameter("patient", patient);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding appointments by patient and status", e);
            throw new RuntimeException("Error finding appointments by patient and status", e);
        }
    }

    @Override
    public List<Appointment> findByDoctorAndStatus(Doctor doctor, AppointmentStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.doctor = :doctor AND a.status = :status AND a.isDeleted = false ORDER BY a.appointmentDateTime DESC",
                    Appointment.class);
            query.setParameter("doctor", doctor);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding appointments by doctor and status", e);
            throw new RuntimeException("Error finding appointments by doctor and status", e);
        }
    }

    @Override
    public List<Appointment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.appointmentDateTime BETWEEN :startDate AND :endDate AND a.isDeleted = false ORDER BY a.appointmentDateTime ASC",
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
    public List<Appointment> findByDoctorAndDateRange(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentDateTime BETWEEN :startDate AND :endDate AND a.isDeleted = false ORDER BY a.appointmentDateTime ASC",
                    Appointment.class);
            query.setParameter("doctor", doctor);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding appointments by doctor and date range", e);
            throw new RuntimeException("Error finding appointments by doctor and date range", e);
        }
    }

    @Override
    public List<Appointment> findTodaysAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(23, 59, 59);

            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.appointmentDateTime BETWEEN :startOfDay AND :endOfDay AND a.isDeleted = false ORDER BY a.appointmentDateTime ASC",
                    Appointment.class);
            query.setParameter("startOfDay", startOfDay);
            query.setParameter("endOfDay", endOfDay);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding today's appointments", e);
            throw new RuntimeException("Error finding today's appointments", e);
        }
    }

    @Override
    public List<Appointment> findUpcomingAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.appointmentDateTime > :currentDateTime AND a.isDeleted = false ORDER BY a.appointmentDateTime ASC",
                    Appointment.class);
            query.setParameter("currentDateTime", LocalDateTime.now());
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding upcoming appointments", e);
            throw new RuntimeException("Error finding upcoming appointments", e);
        }
    }

    @Override
    public List<Appointment> findPastAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.appointmentDateTime < :currentDateTime AND a.isDeleted = false ORDER BY a.appointmentDateTime DESC",
                    Appointment.class);
            query.setParameter("currentDateTime", LocalDateTime.now());
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding past appointments", e);
            throw new RuntimeException("Error finding past appointments", e);
        }
    }

    @Override
    public List<Appointment> findAvailableSlots(Doctor doctor, LocalDateTime date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

            // This is a simplified implementation - in reality, you'd generate available slots
            // based on doctor's schedule and existing appointments
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentDateTime BETWEEN :startOfDay AND :endOfDay AND a.status = :status AND a.isDeleted = false ORDER BY a.appointmentDateTime ASC",
                    Appointment.class);
            query.setParameter("doctor", doctor);
            query.setParameter("startOfDay", startOfDay);
            query.setParameter("endOfDay", endOfDay);
            query.setParameter("status", AppointmentStatus.AVAILABLE);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding available slots", e);
            throw new RuntimeException("Error finding available slots", e);
        }
    }

    @Override
    public List<Appointment> findDeletedAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.isDeleted = true", Appointment.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding deleted appointments", e);
            throw new RuntimeException("Error finding deleted appointments", e);
        }
    }

    @Override
    public long countByStatus(AppointmentStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Appointment a WHERE a.status = :status AND a.isDeleted = false", Long.class);
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
                    "SELECT COUNT(*) FROM Appointment a WHERE a.doctor = :doctor AND a.isDeleted = false", Long.class);
            query.setParameter("doctor", doctor);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting appointments by doctor", e);
            throw new RuntimeException("Error counting appointments by doctor", e);
        }
    }

    @Override
    public long countByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Appointment a WHERE a.patient = :patient AND a.isDeleted = false", Long.class);
            query.setParameter("patient", patient);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting appointments by patient", e);
            throw new RuntimeException("Error counting appointments by patient", e);
        }
    }

    @Override
    public long countTodaysAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(23, 59, 59);

            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Appointment a WHERE a.appointmentDateTime BETWEEN :startOfDay AND :endOfDay AND a.isDeleted = false",
                    Long.class);
            query.setParameter("startOfDay", startOfDay);
            query.setParameter("endOfDay", endOfDay);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting today's appointments", e);
            throw new RuntimeException("Error counting today's appointments", e);
        }
    }

    @Override
    public long countUpcomingAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Appointment a WHERE a.appointmentDateTime > :currentDateTime AND a.isDeleted = false",
                    Long.class);
            query.setParameter("currentDateTime", LocalDateTime.now());
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting upcoming appointments", e);
            throw new RuntimeException("Error counting upcoming appointments", e);
        }
    }

    @Override
    public boolean existsByDoctorAndDateTime(Doctor doctor, LocalDateTime dateTime) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentDateTime = :dateTime AND a.isDeleted = false",
                    Long.class);
            query.setParameter("doctor", doctor);
            query.setParameter("dateTime", dateTime);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            logger.error("Error checking appointment existence by doctor and date time", e);
            throw new RuntimeException("Error checking appointment existence by doctor and date time", e);
        }
    }

    @Override
    public Optional<Appointment> findByDoctorAndDateTime(Doctor doctor, LocalDateTime dateTime) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentDateTime = :dateTime AND a.isDeleted = false",
                    Appointment.class);
            query.setParameter("doctor", doctor);
            query.setParameter("dateTime", dateTime);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding appointment by doctor and date time", e);
            throw new RuntimeException("Error finding appointment by doctor and date time", e);
        }
    }

    @Override
    public List<Appointment> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery("FROM Appointment a WHERE a.isDeleted = false", Appointment.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding all appointments", e);
            throw new RuntimeException("Error finding all appointments", e);
        }
    }
}
