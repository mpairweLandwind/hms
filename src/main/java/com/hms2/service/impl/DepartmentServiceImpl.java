package com.hms2.service.impl;

import com.hms2.model.Department;
import com.hms2.repository.DepartmentRepository;
import com.hms2.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DepartmentServiceImpl implements DepartmentService {
    
    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    
    @Inject
    private DepartmentRepository departmentRepository;
    
    @Override
    public Department createDepartment(Department department) {
        logger.info("Creating new department: {}", department.getDepartmentName());
        
        if (!isDepartmentNameUnique(department.getDepartmentName())) {
            throw new IllegalArgumentException("Department name already exists");
        }
        
        return departmentRepository.save(department);
    }
    
    @Override
    public Department updateDepartment(Department department) {
        logger.info("Updating department: {}", department.getDepartmentName());
        
        Optional<Department> existingDepartment = departmentRepository.findById(department.getDepartmentId());
        if (existingDepartment.isEmpty()) {
            throw new IllegalArgumentException("Department not found");
        }
        
        // Check name uniqueness only if changed
        Department existing = existingDepartment.get();
        if (!existing.getDepartmentName().equals(department.getDepartmentName()) && 
            !isDepartmentNameUnique(department.getDepartmentName())) {
            throw new IllegalArgumentException("Department name already exists");
        }
        
        return departmentRepository.update(department);
    }
    
    @Override
    public void deleteDepartment(Long departmentId) {
        logger.info("Soft deleting department with ID: {}", departmentId);
        Optional<Department> departmentOpt = departmentRepository.findById(departmentId);
        if (departmentOpt.isPresent()) {
            Department department = departmentOpt.get();
            department.softDelete("SYSTEM");
            departmentRepository.update(department);
        } else {
            throw new IllegalArgumentException("Department not found");
        }
    }
    
    @Override
    public void restoreDepartment(Long departmentId) {
        logger.info("Restoring department with ID: {}", departmentId);
        Optional<Department> departmentOpt = departmentRepository.findById(departmentId);
        if (departmentOpt.isPresent()) {
            Department department = departmentOpt.get();
            department.restore("SYSTEM");
            departmentRepository.update(department);
        } else {
            throw new IllegalArgumentException("Department not found");
        }
    }
    
    @Override
    public void permanentlyDeleteDepartment(Long departmentId) {
        logger.info("Permanently deleting department with ID: {}", departmentId);
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
}
