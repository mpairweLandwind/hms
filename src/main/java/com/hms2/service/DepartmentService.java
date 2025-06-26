package com.hms2.service;

import com.hms2.model.Department;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    
    Department createDepartment(Department department);
    
    Department updateDepartment(Department department);
    
    void deleteDepartment(Long departmentId);
    
    void restoreDepartment(Long departmentId);
    
    void permanentlyDeleteDepartment(Long departmentId);
    
    Optional<Department> getDepartmentById(Long departmentId);
    
    List<Department> getAllDepartments();
    
    List<Department> getDeletedDepartments();
    
    List<Department> getDepartmentsByStatus(String status);
    
    List<Department> getDepartmentsByLocation(String location);
    
    Optional<Department> getDepartmentByName(String departmentName);
    
    List<Department> searchDepartmentsByName(String name);
    
    long getDepartmentCountByStatus(String status);
    
    boolean isDepartmentNameUnique(String departmentName);
}
