package com.hms2.service;

import java.util.List;
import java.util.Optional;

import com.hms2.enums.StaffStatus;
import com.hms2.model.Department;
import com.hms2.model.Staff;

public interface StaffService {
    
    Staff createStaff(Staff staff);
    
    Staff updateStaff(Staff staff);

    long getActiveStaffCount();
    
    void deleteStaff(Long staffId);
    
    Optional<Staff> getStaffById(Long staffId);
    
    List<Staff> getAllStaff();
    
    List<Staff> getStaffByDepartment(Department department);
    
    List<Staff> getStaffByStatus(StaffStatus status);
    
    List<Staff> getActiveStaff();
    
    Optional<Staff> getStaffByEmployeeId(String employeeId);
    
    Optional<Staff> getStaffByEmail(String email);
    
    List<Staff> getStaffByPosition(String position);
    
    List<Staff> searchStaffByName(String name);
    
    void verifyStaff(Long staffId);
    
    void rejectStaff(Long staffId);
    
    void deactivateStaff(Long staffId);
    
    void activateStaff(Long staffId);
    
    long getStaffCountByDepartment(Department department);
    
    long getStaffCountByStatus(StaffStatus status);
    
    boolean isEmployeeIdUnique(String employeeId);
    
    boolean isEmailUnique(String email);
    
    // Soft Delete operations
    void restoreStaff(Long staffId);
    List<Staff> getDeletedStaff();
    void permanentlyDeleteStaff(Long staffId);
}
