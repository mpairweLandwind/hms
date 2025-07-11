package com.hms2.service;

import com.hms2.dto.user.DoctorRegistrationDTO;
import com.hms2.dto.user.PatientRegistrationDTO;
import com.hms2.dto.user.StaffRegistrationDTO;
import com.hms2.model.User;

public interface RegistrationService {
    
    // Complete registration methods - creates both User and role-specific entity simultaneously
    User registerPatient(PatientRegistrationDTO patientRegistrationDTO);
    
    User registerDoctor(DoctorRegistrationDTO doctorRegistrationDTO);
    
    User registerStaff(StaffRegistrationDTO staffRegistrationDTO);
    
    // Validation methods
    void validatePatientRegistration(PatientRegistrationDTO patientRegistrationDTO);
    
    void validateDoctorRegistration(DoctorRegistrationDTO doctorRegistrationDTO);
    
    void validateStaffRegistration(StaffRegistrationDTO staffRegistrationDTO);
    
    // Unique constraint checks
    boolean isEmailUnique(String email);
    
    boolean isUsernameUnique(String username);
    
    boolean isEmployeeIdUnique(String employeeId);
    
    boolean isLicenseNumberUnique(String licenseNumber);
}
