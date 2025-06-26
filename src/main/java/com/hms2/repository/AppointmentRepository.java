package com.hms2.repository;

import com.hms2.model.Appointment;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends GenericRepository<Appointment, Long> {
    
    List<Appointment> findByPatient(Patient patient);
    
    List<Appointment> findByDoctor(Doctor doctor);
    
    List<Appointment> findByStatus(String status);
    
    List<Appointment> findByAppointmentType(String appointmentType);
    
    List<Appointment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Appointment> findTodaysAppointments();
    
    List<Appointment> findUpcomingAppointments();
    
    List<Appointment> findDeletedAppointments();
    
    List<Appointment> findConflictingAppointments(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime);
    
    long countByStatus(String status);
    
    long countByDoctor(Doctor doctor);
}
