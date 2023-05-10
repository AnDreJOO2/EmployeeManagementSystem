package com.example.springbackend.service;

import com.example.springbackend.controller.request.EmployeeSearchCriteria;
import com.example.springbackend.exception.EmployeeNotFoundException;
import com.example.springbackend.model.Employee;
import com.example.springbackend.readingwriting.CsvService;
import com.example.springbackend.repository.EmployeeCriteriaRepository;
import com.example.springbackend.repository.EmployeeRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@CacheConfig(cacheNames = {"employee"})
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeCriteriaRepository employeeCriteriaRepository;
    private final CsvService csvService;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeCriteriaRepository employeeCriteriaRepository,
                           CsvService csvService) {
        this.employeeRepository = employeeRepository;
        this.employeeCriteriaRepository = employeeCriteriaRepository;
        this.csvService = csvService;
    }

    public Page<Employee> getEmployees(EmployeeSearchCriteria employeeSearchCriteria) {
        return employeeCriteriaRepository.getEmployeePageWithCriteria(employeeSearchCriteria);
    }

    @Cacheable(key = "#id")
    public Employee getEmployeeById(Long id) {
        return findEmployeeById(id);
    }

    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException(id));
    }

    @CacheEvict(key = "#id", allEntries = true)
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @CachePut(key = "#id")
    public Employee updateEmployeeById(Long id, Employee employee) {
        Employee toUpdate = findEmployeeById(id);
        if (employee.getFirstName() != null) {
            toUpdate.setFirstName(employee.getFirstName());
        }
        if (employee.getLastName() != null) {
            toUpdate.setLastName(employee.getLastName());
        }
        if (employee.getEmail() != null) {
            toUpdate.setEmail(employee.getEmail());
        }
        if (employee.getSalary() != null) {
            toUpdate.setSalary(employee.getSalary());
        }
        return employeeRepository.save(toUpdate);
    }

    @CacheEvict(key = "#id", allEntries = true)
    public void deleteEmployeeById(Long id) {
        Employee toDelete = findEmployeeById(id);
        employeeRepository.delete(toDelete);
    }


    public byte[] exportEmployees(String type) {
        List<Employee> employees = employeeRepository.findAll();
        return csvService.exportEmployeesToCsv(employees);
    }

    public void importEmployees(MultipartFile file) {
        List<Employee> employees = csvService.importEmployees(file);
        saveAllEmployees(employees);
    }

    private void saveAllEmployees(List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }
}
