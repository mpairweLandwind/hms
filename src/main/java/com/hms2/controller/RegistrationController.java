package com.hms2.controller;

import com.hms2.dto.UserRegistrationDTO;
import com.hms2.model.Department;
import com.hms2.model.StaffStatus;
import com.hms2.service.RegistrationService;
import com.hms2.service.DepartmentService;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("registrationController")
@ViewScoped
public class RegistrationController implements Serializable {
    
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    
    @Inject
    private RegistrationService registrationService;
    
    @Inject
    private DepartmentService departmentService;
    
    private UserRegistrationDTO registrationDTO;
    private List<Department> departments;
    private String selectedRole = "PATIENT";
    
    @PostConstruct
    public void init() {
        logger.info("Initializing registration controller");
        registrationDTO = new UserRegistrationDTO();
        registrationDTO.setRole(selectedRole);
        loadDepartments();
    }
    
    private void loadDepartments() {
        try {
            departments = departmentService.getAllDepartments();
            logger.info("Loaded {} departments", departments.size());
        } catch (Exception e) {
            logger.error("Error loading departments", e);
            addErrorMessage("Error loading departments: " + e.getMessage());
        }
    }
    
    public void onRoleChange() {
        logger.info("Role changed to: {}", selectedRole);
        registrationDTO.setRole(selectedRole);
        
        // Clear role-specific fields when role changes
        clearRoleSpecificFields();
    }
    
    private void clearRoleSpecificFields() {
        // Clear patient fields
        registrationDTO.setDateOfBirth(null);
        registrationDTO.setGender(null);
        registrationDTO.setEmergencyContact(null);
        registrationDTO.setEmergencyPhone(null);
        registrationDTO.setInsuranceNumber(null);
        registrationDTO.setBloodType(null);
        registrationDTO.setAllergies(null);
        
        // Clear doctor fields
        registrationDTO.setSpecialization(null);
        registrationDTO.setLicenseNumber(null);
        registrationDTO.setExperience(null);
        registrationDTO.setQualifications(null);
        
        // Clear staff fields
        registrationDTO.setPosition(null);
        registrationDTO.setEmployeeId(null);
        registrationDTO.setHireDate(null);
        registrationDTO.setSalary(null);
        registrationDTO.setNotes(null);
        registrationDTO.setStatus(null);
        
        // Clear department
        registrationDTO.setDepartmentId(null);
    }
    
    public void registerUser() {
        try {
            logger.info("Registering user: {} with role: {}", 
                       registrationDTO.getUsername(), registrationDTO.getRole());
            
            registrationService.registerUser(registrationDTO);
            
            addSuccessMessage("User registered successfully! Account is pending verification.");
            
            // Reset form
            registrationDTO = new UserRegistrationDTO();
            registrationDTO.setRole(selectedRole);
            
            PrimeFaces.current().executeScript("PF('registrationDialog').hide()");
            
        } catch (Exception e) {
            logger.error("Error registering user", e);
            addErrorMessage("Registration failed: " + e.getMessage());
        }
    }
    
    public void validateUsername() {
        if (registrationDTO.getUsername() != null && !registrationDTO.getUsername().trim().isEmpty()) {
            if (!registrationService.isUsernameUnique(registrationDTO.getUsername())) {
                addErrorMessage("Username already exists");
            }
        }
    }
    
    public void validateEmail() {
        if (registrationDTO.getEmail() != null && !registrationDTO.getEmail().trim().isEmpty()) {
            if (!registrationService.isEmailUnique(registrationDTO.getEmail())) {
                addErrorMessage("Email already exists");
            }
        }
    }
    
    public void validateEmployeeId() {
        if (registrationDTO.getEmployeeId() != null && !registrationDTO.getEmployeeId().trim().isEmpty()) {
            if (!registrationService.isEmployeeIdUnique(registrationDTO.getEmployeeId())) {
                addErrorMessage("Employee ID already exists");
            }
        }
    }
    
    public void validateLicenseNumber() {
        if (registrationDTO.getLicenseNumber() != null && !registrationDTO.getLicenseNumber().trim().isEmpty()) {
            if (!registrationService.isLicenseNumberUnique(registrationDTO.getLicenseNumber())) {
                addErrorMessage("License number already exists");
            }
        }
    }
    
    public void prepareNewRegistration() {
        registrationDTO = new UserRegistrationDTO();
        registrationDTO.setRole(selectedRole);
    }
    
    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }
    
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }
    
    // Getters and setters
    public UserRegistrationDTO getRegistrationDTO() {
        return registrationDTO;
    }
    
    public void setRegistrationDTO(UserRegistrationDTO registrationDTO) {
        this.registrationDTO = registrationDTO;
    }
    
    public List<Department> getDepartments() {
        return departments;
    }
    
    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
    
    public String getSelectedRole() {
        return selectedRole;
    }
    
    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }
    
    public String[] getRoles() {
        return new String[]{"PATIENT", "DOCTOR", "STAFF", "ADMIN"};
    }
    
    public String[] getGenders() {
        return new String[]{"MALE", "FEMALE", "OTHER"};
    }
    
    public String[] getBloodTypes() {
        return new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    }
    
    public StaffStatus[] getStaffStatuses() {
        return StaffStatus.values();
    }
    
    public boolean isPatientRole() {
        return "PATIENT".equals(selectedRole);
    }
    
    public boolean isDoctorRole() {
        return "DOCTOR".equals(selectedRole);
    }
    
    public boolean isStaffRole() {
        return "STAFF".equals(selectedRole);
    }
    
    public boolean isAdminRole() {
        return "ADMIN".equals(selectedRole);
    }
}
