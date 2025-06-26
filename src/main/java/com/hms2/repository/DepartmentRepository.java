package com.hms2.repository;

import com.hms2.model.Department;
import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends GenericRepository<Department, Long> {
    
    Optional<Department> findByDepartmentName(String departmentName);
    
    List<Department> findByStatus(String status);
    
    List<Department> findByLocation(String location);
    
    List<Department> searchByName(String name);
    
    List<Department> findDeletedDepartments();
    
    long countByStatus(String status);
}
