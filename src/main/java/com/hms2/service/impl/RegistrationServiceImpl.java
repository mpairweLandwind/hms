package com.hms2.service.impl;

import com.hms2.dto.UserRegistrationDTO;
import com.hms2.model.*;
import com.hms2.repository.*;
import com.hms2.service.AuthenticationService;
import com.hms2.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class RegistrationServiceImpl implements RegistrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);
    
    @Inject
    private AuthenticationService authenticationService;
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private PatientRepository patientRepository;
    
    @Inject
    private DoctorRepository doctorRepository;
    
    @Inject
    private StaffRepository staffRepository;
    
    @Inject
    private DepartmentRepository departmentRepository;
    
    @Override
    public User registerUser(UserRegistrationDTO registrationDTO) {
        logger.info("Registering new user: {}", registrationDTO.getUsername());
        
        validateRegistrationData(registrationDTO);
        
        // Create user account
        User user = authenticationService.createUser(
            registrationDTO.getUsername(),
            registrationDTO.getEmail(),
            registrationDTO.getPassword(),
            registrationDTO.getRole()
        );
        
        // Create role-specific entity
        switch (registrationDTO.getRole()) {
            case "PATIENT":
                registerPatient(registrationDTO);
                break;
            case "DOCTOR":
                registerDoctor(registrationDTO);
                break;
            case "STAFF":
                registerStaff(registrationDTO);
                break;
            case "ADMIN":
                // Admin doesn't need additional entity
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + registrationDTO.getRole());
        }
        
        logger.info("User registered successfully: {}", registrationDTO.getUsername());
        return user;
    }
    
    @Override
    public Patient registerPatient(UserRegistrationDTO registrationDTO) {
        logger.info("Registering patient: {}", registrationDTO.getEmail());
        
        // Find the user account
        Optional<User> userOpt = userRepository.findByEmail(registrationDTO.getEmail());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User account not found");
        }
        
        User user = userOpt.get();
        
        // Create patient entity
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setFirstName(registrationDTO.getFirstName());
        patient.setLastName(registrationDTO.getLastName());
        patient.setEmail(registrationDTO.getEmail());
        patient.setPhoneNumber(registrationDTO.getPhoneNumber());
        patient.setAddress(registrationDTO.getAddress());
        patient.setDateOfBirth(registrationDTO.getDateOfBirth());
        patient.setGender(registrationDTO.getGender());
        patient.setEmergencyContact(registrationDTO.getEmergencyContact());
        patient.setEmergencyPhone(registrationDTO.getEmergencyPhone());
        patient.setInsuranceNumber(registrationDTO.getInsuranceNumber());
        patient.setBloodType(registrationDTO.getBloodType());
        patient.setAllergies(registrationDTO.getAllergies());
        
        return patientRepository.save(patient);
    }
    
    @Override
    public Doctor registerDoctor(UserRegistrationDTO registrationDTO) {
        logger.info("Registering doctor: {}", registrationDTO.getEmail());
        
        // Find the user account
        Optional<User> userOpt = userRepository.findByEmail(registrationDTO.getEmail());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User account not found");
        }
        
        User user = userOpt.get();
        
        // Find department if specified
        Department department = null;
        if (registrationDTO.getDepartmentId() != null) {
            Optional<Department> deptOpt = departmentRepository.findById(registrationDTO.getDepartmentId());
            if (deptOpt.isPresent()) {
                department = deptOpt.get();
            }
        }
        
        // Create doctor entity
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setFirstName(registrationDTO.getFirstName());
        doctor.setLastName(registrationDTO.getLastName());
        doctor.setEmail(registrationDTO.getEmail());
        doctor.setPhoneNumber(registrationDTO.getPhoneNumber());
        doctor.setAddress(registrationDTO.getAddress());
        doctor.setSpecialization(registrationDTO.getSpecialization());
        doctor.setLicenseNumber(registrationDTO.getLicenseNumber());
        doctor.setExperience(registrationDTO.getExperience());
        doctor.setQualifications(registrationDTO.getQualifications());
        doctor.setDepartment(department);
        
        return doctorRepository.save(doctor);
    }
    
    @Override
    public Staff registerStaff(UserRegistrationDTO registrationDTO) {
        logger.info("Registering staff: {}", registrationDTO.getEmail());
        
        // Find the user account
        Optional<User> userOpt = userRepository.findByEmail(registrationDTO.getEmail());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User account not found");
        }
        
        User user = userOpt.get();
        
        // Find department if specified
        Department department = null;
        if (registrationDTO.getDepartmentId() != null) {
            Optional<Department> deptOpt = departmentRepository.findById(registrationDTO.getDepartmentId());
            if (deptOpt.isPresent()) {
                department = deptOpt.get();
            }
        }
        
        // Create staff entity
        Staff staff = new Staff();
        staff.setUser(user);
        staff.setFirstName(registrationDTO.getFirstName());
        staff.setLastName(registrationDTO.getLastName());
        staff.setEmail(registrationDTO.getEmail());
        staff.setPhoneNumber(registrationDTO.getPhoneNumber());
        staff.setAddress(registrationDTO.getAddress());
        staff.setPosition(registrationDTO.getPosition());
        staff.setEmployeeId(registrationDTO.getEmployeeId());
        staff.setHireDate(registrationDTO.getHireDate());
        staff.setSalary(registrationDTO.getSalary());
        staff.setNotes(registrationDTO.getNotes());
        staff.setDepartment(department);
        
        if (registrationDTO.getStatus() != null) {
            staff.setStatus(registrationDTO.getStatus());
        }
        
        return staffRepository.save(staff);
    }
    
    @Override
    public void validateRegistrationData(UserRegistrationDTO registrationDTO) {
        // Validate unique constraints
        if (!isUsernameUnique(registrationDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (!isEmailUnique(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Role-specific validations
        switch (registrationDTO.getRole()) {
            case "DOCTOR":
                if (registrationDTO.getLicenseNumber() != null && 
                    !isLicenseNumberUnique(registrationDTO.getLicenseNumber())) {
                    throw new IllegalArgumentException("License number already exists");
                }
                break;
            case "STAFF":
                if (registrationDTO.getEmployeeId() != null && 
                    !isEmployeeIdUnique(registrationDTO.getEmployeeId())) {
                    throw new IllegalArgumentException("Employee ID already exists");
                }
                break;
        }
    }
    
    @Override
    public boolean isEmailUnique(String email) {
        return authenticationService.isEmailAvailable(email);
    }
    
    @Override
    public boolean isUsernameUnique(String username) {
        return authenticationService.isUsernameAvailable(username);
    }
    
    @Override
    public boolean isEmployeeIdUnique(String employeeId) {
        return staffRepository.findByEmployeeId(employeeId).isEmpty();
    }
    
    @Override
    public boolean isLicenseNumberUnique(String licenseNumber) {
        return doctorRepository.findByLicenseNumber(licenseNumber).isEmpty();
    }
}
