package com.hms2.service.impl;

import com.hms2.dto.dashboard.DashboardAlertDTO;
import com.hms2.dto.dashboard.QuickActionDTO;
import com.hms2.model.Appointment;
import com.hms2.model.Department;
import com.hms2.model.Doctor;
import com.hms2.model.Patient;
import com.hms2.model.Prescription;
import com.hms2.repository.DoctorRepository;
import com.hms2.service.DoctorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.*;

@ApplicationScoped
public class DoctorServiceImpl implements DoctorService {
    @Inject
    private DoctorRepository doctorRepository;

    @Override
    public Doctor createDoctor(Doctor doctor) {
        System.err.println("[DoctorServiceImpl] Registering doctor: " + doctor.getEmail());
        try {
            Doctor savedDoctor = doctorRepository.save(doctor);
            System.err.println("[DoctorServiceImpl] Doctor registered successfully: " + doctor.getEmail());
            return savedDoctor;
        } catch (Exception e) {
            System.err.println("[DoctorServiceImpl] Error registering doctor: " + doctor.getEmail() + ", error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Doctor updateDoctor(Doctor doctor) {
        return doctorRepository.update(doctor);
    }

    @Override
    public void deleteDoctor(Long doctorId) {
        doctorRepository.findById(doctorId).ifPresent(doctor -> {
            doctor.setIsDeleted(true);
            doctorRepository.update(doctor);
        });
    }

    @Override
    public void restoreDoctor(Long doctorId) {
        doctorRepository.findById(doctorId).ifPresent(doctor -> {
            doctor.setIsDeleted(false);
            doctorRepository.update(doctor);
        });
    }

    @Override
    public void permanentlyDeleteDoctor(Long doctorId) {
        doctorRepository.deleteById(doctorId);
    }

    @Override
    public Doctor findById(Long doctorId) {
        return doctorRepository.findById(doctorId).orElse(null);
    }

    @Override
    public Optional<Doctor> getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public List<Doctor> getDeletedDoctors() {
        return doctorRepository.findDeletedDoctors();
    }

    @Override
    public List<Doctor> getAllDoctorsWithDepartments() {
        // Use findAll instead, since department is not used anymore
        return doctorRepository.findAll();
    }

    @Override
    public List<Doctor> getDeletedDoctorsWithDepartments() {
        // Use findDeletedDoctors instead
        return doctorRepository.findDeletedDoctors();
    }

    @Override
    public List<Doctor> getActiveDoctors() {
        return doctorRepository.findByActive(true);
    }

    @Override
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    @Override
    public List<Doctor> getDoctorsByStatus(String status) {
        return doctorRepository.findByStatus(status);
    }

    @Override
    public Optional<Doctor> getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    @Override
    public Optional<Doctor> getDoctorByLicenseNumber(String licenseNumber) {
        return doctorRepository.findByLicenseNumber(licenseNumber);
    }

    @Override
    public List<Doctor> searchDoctorsByName(String name) {
        return doctorRepository.searchByName(name);
    }

   

    @Override
    public long getDoctorCountBySpecialization(String specialization) {
        return doctorRepository.countBySpecialization(specialization);
    }

    @Override
    public long getDoctorCountByStatus(String status) {
        return doctorRepository.countByStatus(status);
    }

    @Override
    public boolean isEmailUnique(String email) {
        return doctorRepository.findByEmail(email).isEmpty();
    }

    @Override
    public boolean isLicenseNumberUnique(String licenseNumber) {
        return doctorRepository.findByLicenseNumber(licenseNumber).isEmpty();
    }

    @Override
    public long getActiveDoctorCount() {
        return doctorRepository.findByActive(true).size();
    }

    @Override
    public Map<String, Long> getDashboardStatistics(Long doctorId) {
        return new HashMap<>();
    }

    @Override
    public Map<String, Double> getDashboardMetrics(Long doctorId) {
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getChartData(Long doctorId) {
        return new HashMap<>();
    }

    @Override
    public List<QuickActionDTO> getQuickActions(Long doctorId) {
        return new ArrayList<>();
    }

    @Override
    public List<DashboardAlertDTO> getAlerts(Long doctorId) {
        return new ArrayList<>();
    }

    @Override
    public List<Appointment> getTodaysAppointments(Long doctorId) {
        return new ArrayList<>();
    }

    @Override
    public List<Appointment> getUpcomingAppointments(Long doctorId) {
        return new ArrayList<>();
    }

    @Override
    public List<Patient> getRecentPatients(Long doctorId) {
        return new ArrayList<>();
    }

    @Override
    public List<Prescription> getRecentPrescriptions(Long doctorId) {
        return new ArrayList<>();
    }

    @Override
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor findByUserId(Long userId) {
        return doctorRepository.findByUserId(userId).orElse(null);
    }
}
