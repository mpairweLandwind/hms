package com.hms2.service;

import com.hms2.model.Staff;
import com.hms2.model.StaffStatus;
import com.hms2.model.Department;
import java.util.List;
import java.util.Optional;

public interface StaffService {
    
    Staff createStaff(Staff staff);
    
    Staff updateStaff(Staff staff);
    
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
}
