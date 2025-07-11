package com.hms2.repository;

import com.hms2.model.Staff;
import com.hms2.enums.StaffStatus;
import com.hms2.model.Department;
import java.util.List;
import java.util.Optional;

public interface StaffRepository extends GenericRepository<Staff, Long> {
    
    List<Staff> findByDepartment(Department department);
    
    List<Staff> findByStatus(StaffStatus status);
    
    List<Staff> findByActive(boolean active);
    
    Optional<Staff> findByEmployeeId(String employeeId);
    
    Optional<Staff> findByEmail(String email);
    
    List<Staff> findByPosition(String position);
    
    List<Staff> searchByName(String name);
    
    long countByDepartment(Department department);
    
    long countByStatus(StaffStatus status);

    List<Staff> findDeletedStaff();
}
