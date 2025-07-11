package com.hms2.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hms2.dto.user.StaffRegistrationDTO;
import com.hms2.enums.StaffStatus;
import com.hms2.model.Department;
import com.hms2.service.DepartmentService;
import com.hms2.service.RegistrationService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

@Named("staffRegistrationController")
@RequestScoped
public class StaffRegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(StaffRegistrationController.class);

    @Inject
    private RegistrationService registrationService;

    @Inject
    private DepartmentService departmentService;

    private StaffRegistrationDTO registrationDTO;
    private List<Department> departments;
    private Date today = new Date();

    @PostConstruct
    public void init() {
        logger.info("Initializing staff registration controller");
        registrationDTO = new StaffRegistrationDTO();
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

    public String registerStaff() {
        try {
            logger.info("Registering staff: {}", registrationDTO.getEmail());

            // Complete registration process - creates both User and Staff simultaneously
            registrationService.registerStaff(registrationDTO);

            addSuccessMessage("Staff application submitted successfully! HR will review your application and contact you soon.");

            // Redirect to the same registration page on success
            return "staff-registration?faces-redirect=true";

        } catch (Exception e) {
            logger.error("Error registering staff", e);
            addErrorMessage("Registration failed: " + e.getMessage());
            
            // Stay on the same page if there's an error
            return null;
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

    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }

    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }

    // Getters and setters
    public StaffRegistrationDTO getRegistrationDTO() {
        return registrationDTO;
    }

    public void setRegistrationDTO(StaffRegistrationDTO registrationDTO) {
        this.registrationDTO = registrationDTO;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public Date getToday() {
        return today;
    }

    // Helper methods for UI
    public StaffStatus[] getStaffStatuses() {
        return StaffStatus.values();
    }
}
