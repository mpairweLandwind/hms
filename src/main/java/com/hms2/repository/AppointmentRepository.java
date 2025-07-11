package com.hms2.repository;

import com.hms2.model.Appointment;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.enums.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends GenericRepository<Appointment, Long> {

    List<Appointment> findByPatient(Patient patient);

    List<Appointment> findByDoctor(Doctor doctor);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByPatientAndStatus(Patient patient, AppointmentStatus status);

    List<Appointment> findByDoctorAndStatus(Doctor doctor, AppointmentStatus status);

    List<Appointment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> findByDoctorAndDateRange(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> findTodaysAppointments();

    List<Appointment> findUpcomingAppointments();

    List<Appointment> findPastAppointments();

    List<Appointment> findAvailableSlots(Doctor doctor, LocalDateTime date);

    List<Appointment> findDeletedAppointments();

    long countByStatus(AppointmentStatus status);

    long countByDoctor(Doctor doctor);

    long countByPatient(Patient patient);

    long countTodaysAppointments();

    long countUpcomingAppointments();

    boolean existsByDoctorAndDateTime(Doctor doctor, LocalDateTime dateTime);

    Optional<Appointment> findByDoctorAndDateTime(Doctor doctor, LocalDateTime dateTime);
}
