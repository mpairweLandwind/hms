package com.hms2.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.dto.user.DoctorRegistrationDTO;
import com.hms2.dto.user.PatientRegistrationDTO;
import com.hms2.dto.user.StaffRegistrationDTO;
import com.hms2.enums.UserRole;
import com.hms2.model.Department;
import com.hms2.model.Doctor;
import com.hms2.model.Patient;
import com.hms2.model.Staff;
import com.hms2.model.User;
import com.hms2.repository.DepartmentRepository;
import com.hms2.repository.DoctorRepository;
import com.hms2.repository.PatientRepository;
import com.hms2.repository.StaffRepository;
import com.hms2.repository.UserRepository;
import com.hms2.service.AuthenticationService;
import com.hms2.service.RegistrationService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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
    @Transactional
    public User registerPatient(PatientRegistrationDTO patientRegistrationDTO) {
        logger.info("Starting patient registration: {}", patientRegistrationDTO.getEmail());
        
        // Validate registration data
        validatePatientRegistration(patientRegistrationDTO);
        
        // Create user account first
        User user = authenticationService.createUser(
            patientRegistrationDTO.getUsername(),
            patientRegistrationDTO.getEmail(),
            patientRegistrationDTO.getPassword(),
            UserRole.PATIENT.name()
        );
        
        // Create patient entity
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setFirstName(patientRegistrationDTO.getFirstName());
        patient.setLastName(patientRegistrationDTO.getLastName());
        patient.setEmail(patientRegistrationDTO.getEmail());
        patient.setPhoneNumber(patientRegistrationDTO.getPhoneNumber());
        patient.setAddress(patientRegistrationDTO.getAddress());
        patient.setDateOfBirth(patientRegistrationDTO.getDateOfBirth());
        patient.setGender(patientRegistrationDTO.getGender());
        patient.setEmergencyContact(patientRegistrationDTO.getEmergencyContact());
        patient.setEmergencyPhone(patientRegistrationDTO.getEmergencyPhone());
        patient.setInsuranceNumber(patientRegistrationDTO.getInsuranceNumber());
        patient.setBloodType(patientRegistrationDTO.getBloodType());
        patient.setAllergies(patientRegistrationDTO.getAllergies());
        
        Patient savedPatient = patientRepository.save(patient);
        logger.info("Patient registration completed successfully: {}", savedPatient.getEmail());
        
        return user;
    }
    
    @Override
    @Transactional
    public User registerDoctor(DoctorRegistrationDTO doctorRegistrationDTO) {
        System.err.println("[RegistrationServiceImpl] Starting doctor registration: " + doctorRegistrationDTO.getEmail());
        
        // Validate registration data
        validateDoctorRegistration(doctorRegistrationDTO);
        
        // Create user account first
        User user = authenticationService.createUser(
            doctorRegistrationDTO.getUsername(),
            doctorRegistrationDTO.getEmail(),
            doctorRegistrationDTO.getPassword(),
            UserRole.DOCTOR.name()
        );
        
        System.err.println("[RegistrationServiceImpl] User created successfully: " + user.getUsername());
        
        // Find department if specified
        Department department = null;
        if (doctorRegistrationDTO.getDepartment() != null && !doctorRegistrationDTO.getDepartment().isEmpty()) {
            Optional<Department> deptOpt = departmentRepository.findByDepartmentName(doctorRegistrationDTO.getDepartment());
            if (deptOpt.isPresent()) {
                department = deptOpt.get();
                System.err.println("[RegistrationServiceImpl] Department found: " + department.getDepartmentName());
            } else {
                System.err.println("[RegistrationServiceImpl] Department not found: " + doctorRegistrationDTO.getDepartment());
            }
        }
        
        // Create doctor entity
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setFirstName(doctorRegistrationDTO.getFirstName());
        doctor.setLastName(doctorRegistrationDTO.getLastName());
        doctor.setEmail(doctorRegistrationDTO.getEmail());
        doctor.setPhoneNumber(doctorRegistrationDTO.getPhoneNumber());
        doctor.setAddress(doctorRegistrationDTO.getAddress());
        doctor.setSpecialization(doctorRegistrationDTO.getSpecialization());
        doctor.setLicenseNumber(doctorRegistrationDTO.getLicenseNumber());
        doctor.setExperience(doctorRegistrationDTO.getExperience());
        doctor.setQualifications(doctorRegistrationDTO.getQualifications());
        
        System.err.println("[RegistrationServiceImpl] Doctor entity created, saving to database...");
        Doctor savedDoctor = doctorRepository.save(doctor);
        System.err.println("[RegistrationServiceImpl] Doctor registration completed successfully: " + savedDoctor.getEmail());
        
        return user;
    }
    
    @Override
    @Transactional
    public User registerStaff(StaffRegistrationDTO staffRegistrationDTO) {
        logger.info("Starting staff registration: {}", staffRegistrationDTO.getEmail());
        
        // Validate registration data
        validateStaffRegistration(staffRegistrationDTO);
        
        // Create user account first
        User user = authenticationService.createUser(
            staffRegistrationDTO.getUsername(),
            staffRegistrationDTO.getEmail(),
            staffRegistrationDTO.getPassword(),
            UserRole.STAFF.name()
        );
        
        // Find department if specified
        Department department = null;
        if (staffRegistrationDTO.getDepartmentName() != null && !staffRegistrationDTO.getDepartmentName().isEmpty()) {
            Optional<Department> deptOpt = departmentRepository.findByDepartmentName(staffRegistrationDTO.getDepartmentName());
            if (deptOpt.isPresent()) {
                department = deptOpt.get();
            }
        }
        
        // Create staff entity
        Staff staff = new Staff();
        staff.setUser(user);
        staff.setFirstName(staffRegistrationDTO.getFirstName());
        staff.setLastName(staffRegistrationDTO.getLastName());
        staff.setEmail(staffRegistrationDTO.getEmail());
        staff.setPhoneNumber(staffRegistrationDTO.getPhoneNumber());
        staff.setAddress(staffRegistrationDTO.getAddress());
        staff.setPosition(staffRegistrationDTO.getPosition());
        staff.setEmployeeId(staffRegistrationDTO.getEmployeeId());
        staff.setHireDate(staffRegistrationDTO.getHireDate());
        staff.setSalary(staffRegistrationDTO.getSalary());
        staff.setNotes(staffRegistrationDTO.getNotes());
        staff.setDepartment(department);
        staff.setStatus(staffRegistrationDTO.getStatus());
        
        Staff savedStaff = staffRepository.save(staff);
        logger.info("Staff registration completed successfully: {}", savedStaff.getEmail());
        
        return user;
    }
    
    @Override
    public void validatePatientRegistration(PatientRegistrationDTO patientRegistrationDTO) {
        logger.info("Validating patient registration: {}", patientRegistrationDTO.getEmail());
        
        // Validate unique constraints
        if (!isUsernameUnique(patientRegistrationDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (!isEmailUnique(patientRegistrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Validate password match
        if (!patientRegistrationDTO.isPasswordMatching()) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        
        // Validate required patient fields
        if (patientRegistrationDTO.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }
        
        if (patientRegistrationDTO.getGender() == null || patientRegistrationDTO.getGender().trim().isEmpty()) {
            throw new IllegalArgumentException("Gender is required");
        }
        
        logger.info("Patient registration validation passed: {}", patientRegistrationDTO.getEmail());
    }
    
    @Override
    public void validateDoctorRegistration(DoctorRegistrationDTO doctorRegistrationDTO) {
        System.err.println("[RegistrationServiceImpl] Validating doctor registration: " + doctorRegistrationDTO.getEmail());
        
        // Validate unique constraints
        if (!isUsernameUnique(doctorRegistrationDTO.getUsername())) {
            System.err.println("[RegistrationServiceImpl] Username already exists: " + doctorRegistrationDTO.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (!isEmailUnique(doctorRegistrationDTO.getEmail())) {
            System.err.println("[RegistrationServiceImpl] Email already exists: " + doctorRegistrationDTO.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Validate password match
        if (!doctorRegistrationDTO.isPasswordMatching()) {
            System.err.println("[RegistrationServiceImpl] Passwords do not match for: " + doctorRegistrationDTO.getEmail());
            throw new IllegalArgumentException("Passwords do not match");
        }
        
        // Validate license number uniqueness
        if (doctorRegistrationDTO.getLicenseNumber() != null && 
            !isLicenseNumberUnique(doctorRegistrationDTO.getLicenseNumber())) {
            System.err.println("[RegistrationServiceImpl] License number already exists: " + doctorRegistrationDTO.getLicenseNumber());
            throw new IllegalArgumentException("License number already exists");
        }
        
        // Validate required doctor fields
        if (doctorRegistrationDTO.getSpecialization() == null || doctorRegistrationDTO.getSpecialization().trim().isEmpty()) {
            System.err.println("[RegistrationServiceImpl] Specialization is missing for: " + doctorRegistrationDTO.getEmail());
            throw new IllegalArgumentException("Specialization is required");
        }
        
        if (doctorRegistrationDTO.getLicenseNumber() == null || doctorRegistrationDTO.getLicenseNumber().trim().isEmpty()) {
            System.err.println("[RegistrationServiceImpl] License number is missing for: " + doctorRegistrationDTO.getEmail());
            throw new IllegalArgumentException("License number is required");
        }
        
        System.err.println("[RegistrationServiceImpl] Doctor registration validation passed: " + doctorRegistrationDTO.getEmail());
    }
    
    @Override
    public void validateStaffRegistration(StaffRegistrationDTO staffRegistrationDTO) {
        logger.info("Validating staff registration: {}", staffRegistrationDTO.getEmail());
        
        // Validate unique constraints
        if (!isUsernameUnique(staffRegistrationDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (!isEmailUnique(staffRegistrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Validate password match
        if (!staffRegistrationDTO.isPasswordMatching()) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        
        // Validate employee ID uniqueness
        if (staffRegistrationDTO.getEmployeeId() != null && 
            !isEmployeeIdUnique(staffRegistrationDTO.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }
        
        // Validate required staff fields
        if (staffRegistrationDTO.getPosition() == null || staffRegistrationDTO.getPosition().trim().isEmpty()) {
            throw new IllegalArgumentException("Position is required");
        }
        
        if (staffRegistrationDTO.getEmployeeId() == null || staffRegistrationDTO.getEmployeeId().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        
        if (staffRegistrationDTO.getHireDate() == null) {
            throw new IllegalArgumentException("Hire date is required");
        }
        
        logger.info("Staff registration validation passed: {}", staffRegistrationDTO.getEmail());
    }
    
    @Override
    public boolean isEmailUnique(String email) {
        boolean isUnique = authenticationService.isEmailAvailable(email);
        System.err.println("[RegistrationServiceImpl] Email uniqueness check for " + email + ": " + isUnique);
        return isUnique;
    }
    
    @Override
    public boolean isUsernameUnique(String username) {
        boolean isUnique = authenticationService.isUsernameAvailable(username);
        System.err.println("[RegistrationServiceImpl] Username uniqueness check for " + username + ": " + isUnique);
        return isUnique;
    }
    
    @Override
    public boolean isEmployeeIdUnique(String employeeId) {
        boolean isUnique = staffRepository.findByEmployeeId(employeeId).isEmpty();
        System.err.println("[RegistrationServiceImpl] Employee ID uniqueness check for " + employeeId + ": " + isUnique);
        return isUnique;
    }
    
    @Override
    public boolean isLicenseNumberUnique(String licenseNumber) {
        boolean isUnique = doctorRepository.findByLicenseNumber(licenseNumber).isEmpty();
        System.err.println("[RegistrationServiceImpl] License number uniqueness check for " + licenseNumber + ": " + isUnique);
        return isUnique;
    }
}
