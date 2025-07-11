package com.hms2.controller;

import com.hms2.model.Staff;
import com.hms2.enums.StaffStatus;
import com.hms2.service.StaffService;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Named("staffController")
@RequestScoped
public class StaffController {
    
    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);
    
    @Inject
    private StaffService staffService;
    
    private List<Staff> staffList;
    private List<Staff> filteredStaff;
    private Staff selectedStaff;
    private Staff newStaff;
    private String searchTerm;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing staff controller");
        loadStaffList();
        newStaff = new Staff();
    }
    
    private void loadStaffList() {
        try {
            staffList = staffService.getAllStaff();
            logger.info("Loaded {} staff members", staffList.size());
        } catch (Exception e) {
            logger.error("Error loading staff list", e);
            addErrorMessage("Error loading staff list: " + e.getMessage());
        }
    }
    
    public void searchStaff() {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            try {
                staffList = staffService.searchStaffByName(searchTerm.trim());
                logger.info("Search returned {} results for term: {}", staffList.size(), searchTerm);
            } catch (Exception e) {
                logger.error("Error searching staff", e);
                addErrorMessage("Error searching staff: " + e.getMessage());
            }
        } else {
            loadStaffList();
        }
    }
    
    public void clearSearch() {
        searchTerm = "";
        loadStaffList();
    }
    
    public void createStaff() {
        try {
            staffService.createStaff(newStaff);
            addSuccessMessage("Staff member created successfully");
            loadStaffList();
            newStaff = new Staff();
            PrimeFaces.current().executeScript("PF('staffDialog').hide()");
        } catch (Exception e) {
            logger.error("Error creating staff", e);
            addErrorMessage("Error creating staff: " + e.getMessage());
        }
    }
    
    public void updateStaff() {
        try {
            staffService.updateStaff(selectedStaff);
            addSuccessMessage("Staff member updated successfully");
            loadStaffList();
            PrimeFaces.current().executeScript("PF('editStaffDialog').hide()");
        } catch (Exception e) {
            logger.error("Error updating staff", e);
            addErrorMessage("Error updating staff: " + e.getMessage());
        }
    }
    
    public void deleteStaff() {
        try {
            staffService.deleteStaff(selectedStaff.getStaffId());
            addSuccessMessage("Staff member deleted successfully");
            loadStaffList();
        } catch (Exception e) {
            logger.error("Error deleting staff", e);
            addErrorMessage("Error deleting staff: " + e.getMessage());
        }
    }
    
    public void verifyStaff(Staff staff) {
        try {
            staffService.verifyStaff(staff.getStaffId());
            addSuccessMessage("Staff member verified successfully");
            loadStaffList();
        } catch (Exception e) {
            logger.error("Error verifying staff", e);
            addErrorMessage("Error verifying staff: " + e.getMessage());
        }
    }
    
    public void rejectStaff(Staff staff) {
        try {
            staffService.rejectStaff(staff.getStaffId());
            addSuccessMessage("Staff member rejected");
            loadStaffList();
        } catch (Exception e) {
            logger.error("Error rejecting staff", e);
            addErrorMessage("Error rejecting staff: " + e.getMessage());
        }
    }
    
    public void toggleStaffStatus(Staff staff) {
        try {
            if (staff.isActive()) {
                staffService.deactivateStaff(staff.getStaffId());
                addSuccessMessage("Staff member deactivated");
            } else {
                staffService.activateStaff(staff.getStaffId());
                addSuccessMessage("Staff member activated");
            }
            loadStaffList();
        } catch (Exception e) {
            logger.error("Error toggling staff status", e);
            addErrorMessage("Error updating staff status: " + e.getMessage());
        }
    }
    
    public void prepareNewStaff() {
        newStaff = new Staff();
    }
    
    public void prepareEditStaff(Staff staff) {
        selectedStaff = staff;
    }
    
    public void prepareDeleteStaff(Staff staff) {
        selectedStaff = staff;
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
    public List<Staff> getStaffList() {
        return staffList;
    }
    
    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }
    
    public List<Staff> getFilteredStaff() {
        return filteredStaff;
    }
    
    public void setFilteredStaff(List<Staff> filteredStaff) {
        this.filteredStaff = filteredStaff;
    }
    
    public Staff getSelectedStaff() {
        return selectedStaff;
    }
    
    public void setSelectedStaff(Staff selectedStaff) {
        this.selectedStaff = selectedStaff;
    }
    
    public Staff getNewStaff() {
        return newStaff;
    }
    
    public void setNewStaff(Staff newStaff) {
        this.newStaff = newStaff;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    public StaffStatus[] getStaffStatuses() {
        return StaffStatus.values();
    }
}
