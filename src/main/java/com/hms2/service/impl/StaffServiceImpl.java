package com.hms2.service.impl;

import com.hms2.model.Staff;
import com.hms2.model.StaffStatus;
import com.hms2.model.Department;
import com.hms2.repository.StaffRepository;
import com.hms2.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StaffServiceImpl implements StaffService {
    
    private static final Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);
    
    @Inject
    private StaffRepository staffRepository;
    
    @Override
    public Staff createStaff(Staff staff) {
        logger.info("Creating new staff: {}", staff.getFullName());
        
        // Validate unique constraints
        if (!isEmployeeIdUnique(staff.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }
        
        if (!isEmailUnique(staff.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        return staffRepository.save(staff);
    }
    
    @Override
    public Staff updateStaff(Staff staff) {
        logger.info("Updating staff: {}", staff.getFullName());
        
        Optional<Staff> existingStaff = staffRepository.findById(staff.getStaffId());
        if (existingStaff.isEmpty()) {
            throw new IllegalArgumentException("Staff not found");
        }
        
        // Check unique constraints only if values changed
        Staff existing = existingStaff.get();
        if (!existing.getEmployeeId().equals(staff.getEmployeeId()) && 
            !isEmployeeIdUnique(staff.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }
        
        if (!existing.getEmail().equals(staff.getEmail()) && 
            !isEmailUnique(staff.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        return staffRepository.update(staff);
    }
    
    @Override
    public void deleteStaff(Long staffId) {
        logger.info("Soft deleting staff with ID: {}", staffId);
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.softDelete("SYSTEM"); // In real app, get current user
            staffRepository.update(staff);
        } else {
            throw new IllegalArgumentException("Staff not found");
        }
    }

    public void restoreStaff(Long staffId) {
        logger.info("Restoring staff with ID: {}", staffId);
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.restore("SYSTEM"); // In real app, get current user
            staffRepository.update(staff);
        } else {
            throw new IllegalArgumentException("Staff not found");
        }
    }

    public List<Staff> getDeletedStaff() {
        return staffRepository.findDeletedStaff();
    }

    public void permanentlyDeleteStaff(Long staffId) {
        logger.info("Permanently deleting staff with ID: {}", staffId);
        staffRepository.deleteById(staffId);
    }
    
    @Override
    public Optional<Staff> getStaffById(Long staffId) {
        return staffRepository.findById(staffId);
    }
    
    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }
    
    @Override
    public List<Staff> getStaffByDepartment(Department department) {
        return staffRepository.findByDepartment(department);
    }
    
    @Override
    public List<Staff> getStaffByStatus(StaffStatus status) {
        return staffRepository.findByStatus(status);
    }
    
    @Override
    public List<Staff> getActiveStaff() {
        return staffRepository.findByActive(true);
    }
    
    @Override
    public Optional<Staff> getStaffByEmployeeId(String employeeId) {
        return staffRepository.findByEmployeeId(employeeId);
    }
    
    @Override
    public Optional<Staff> getStaffByEmail(String email) {
        return staffRepository.findByEmail(email);
    }
    
    @Override
    public List<Staff> getStaffByPosition(String position) {
        return staffRepository.findByPosition(position);
    }
    
    @Override
    public List<Staff> searchStaffByName(String name) {
        return staffRepository.searchByName(name);
    }
    
    @Override
    public void verifyStaff(Long staffId) {
        logger.info("Verifying staff with ID: {}", staffId);
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.verify();
            staffRepository.update(staff);
        } else {
            throw new IllegalArgumentException("Staff not found");
        }
    }
    
    @Override
    public void rejectStaff(Long staffId) {
        logger.info("Rejecting staff with ID: {}", staffId);
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.reject();
            staffRepository.update(staff);
        } else {
            throw new IllegalArgumentException("Staff not found");
        }
    }
    
    @Override
    public void deactivateStaff(Long staffId) {
        logger.info("Deactivating staff with ID: {}", staffId);
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.setActive(false);
            staffRepository.update(staff);
        } else {
            throw new IllegalArgumentException("Staff not found");
        }
    }
    
    @Override
    public void activateStaff(Long staffId) {
        logger.info("Activating staff with ID: {}", staffId);
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.setActive(true);
            staffRepository.update(staff);
        } else {
            throw new IllegalArgumentException("Staff not found");
        }
    }
    
    @Override
    public long getStaffCountByDepartment(Department department) {
        return staffRepository.countByDepartment(department);
    }
    
    @Override
    public long getStaffCountByStatus(StaffStatus status) {
        return staffRepository.countByStatus(status);
    }
    
    @Override
    public boolean isEmployeeIdUnique(String employeeId) {
        return staffRepository.findByEmployeeId(employeeId).isEmpty();
    }
    
    @Override
    public boolean isEmailUnique(String email) {
        return staffRepository.findByEmail(email).isEmpty();
    }
}
