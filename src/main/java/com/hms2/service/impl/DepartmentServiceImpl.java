package com.hms2.service.impl;

import java.util.List;
import java.util.Optional;

import com.hms2.model.Department;
import com.hms2.repository.DepartmentRepository;
import com.hms2.service.DepartmentService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DepartmentServiceImpl implements DepartmentService {
    
    @Inject
    private DepartmentRepository departmentRepository;
    
    @Override
    public Department createDepartment(Department department) {
        System.err.println("[CREATE] Department received from form: " + department);
        try {
            System.err.println("Creating new department: " + department.getDepartmentName());
        
        if (!isDepartmentNameUnique(department.getDepartmentName())) {
            throw new IllegalArgumentException("Department name already exists");
        }
        
            System.err.println("[SUCCESS] Department created: " + department);
        return departmentRepository.save(department);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to create department: " + department);
            e.printStackTrace(System.err);
            throw e;
        }
    }
    
    @Override
    public Department updateDepartment(Department department) {
        System.err.println("[UPDATE] Department received from form: " + department);
        try {
            System.err.println("Updating department: " + department.getDepartmentName());
        
        Optional<Department> existingDepartment = departmentRepository.findById(department.getId());
        if (existingDepartment.isEmpty()) {
            throw new IllegalArgumentException("Department not found");
        }
        
        // Check name uniqueness only if changed
        Department existing = existingDepartment.get();
        if (!existing.getDepartmentName().equals(department.getDepartmentName()) && 
            !isDepartmentNameUnique(department.getDepartmentName())) {
            throw new IllegalArgumentException("Department name already exists");
        }
        
            System.err.println("[SUCCESS] Department updated: " + department);
        return departmentRepository.update(department);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to update department: " + department);
            e.printStackTrace(System.err);
            throw e;
        }
    }
    
    @Override
    public void deleteDepartment(Long departmentId) {
        System.err.println("[DELETE] Department delete requested for ID: " + departmentId);
        try {
            System.err.println("Soft deleting department with ID: " + departmentId);
        Optional<Department> departmentOpt = departmentRepository.findById(departmentId);
        if (departmentOpt.isPresent()) {
            Department department = departmentOpt.get();
            department.softDelete("SYSTEM");
            departmentRepository.update(department);
                System.err.println("[SUCCESS] Department soft deleted: " + departmentId);
        } else {
            throw new IllegalArgumentException("Department not found");
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to soft delete department: " + departmentId);
            e.printStackTrace(System.err);
            throw e;
        }
    }
    
    @Override
    public void restoreDepartment(Long departmentId) {
        System.err.println("Restoring department with ID: " + departmentId);
        Optional<Department> departmentOpt = departmentRepository.findById(departmentId);
        if (departmentOpt.isPresent()) {
            Department department = departmentOpt.get();
            department.restore();
            departmentRepository.update(department);
        } else {
            throw new IllegalArgumentException("Department not found");
        }
    }
    
    @Override
    public void permanentlyDeleteDepartment(Long departmentId) {
        System.err.println("Permanently deleting department with ID: " + departmentId);
        departmentRepository.deleteById(departmentId);
    }
    
    @Override
    public Optional<Department> getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId);
    }
    
    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    @Override
    public List<Department> getDeletedDepartments() {
        return departmentRepository.findDeletedDepartments();
    }
    
    @Override
    public List<Department> getDepartmentsByStatus(String status) {
        return departmentRepository.findByStatus(status);
    }
    
    @Override
    public List<Department> getDepartmentsByLocation(String location) {
        return departmentRepository.findByLocation(location);
    }
    
    @Override
    public Optional<Department> getDepartmentByName(String departmentName) {
        return departmentRepository.findByDepartmentName(departmentName);
    }
    
    @Override
    public List<Department> searchDepartmentsByName(String name) {
        return departmentRepository.searchByName(name);
    }
    
    @Override
    public long getDepartmentCountByStatus(String status) {
        return departmentRepository.countByStatus(status);
    }
    
    @Override
    public boolean isDepartmentNameUnique(String departmentName) {
        return departmentRepository.findByDepartmentName(departmentName).isEmpty();
    }

    @Override
    public List<Department> findAll() {
        return List.of();
    }
}
