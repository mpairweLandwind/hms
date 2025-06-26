package com.hms2.service;

import com.hms2.dto.UserRegistrationDTO;
import com.hms2.model.User;
import com.hms2.model.Patient;
import com.hms2.model.Doctor;
import com.hms2.model.Staff;

public interface RegistrationService {
    
    User registerUser(UserRegistrationDTO registrationDTO);
    
    Patient registerPatient(UserRegistrationDTO registrationDTO);
    
    Doctor registerDoctor(UserRegistrationDTO registrationDTO);
    
    Staff registerStaff(UserRegistrationDTO registrationDTO);
    
    void validateRegistrationData(UserRegistrationDTO registrationDTO);
    
    boolean isEmailUnique(String email);
    
    boolean isUsernameUnique(String username);
    
    boolean isEmployeeIdUnique(String employeeId);
    
    boolean isLicenseNumberUnique(String licenseNumber);
}
