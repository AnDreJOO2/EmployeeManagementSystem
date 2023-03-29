package com.example.springbackend.service;

import com.example.springbackend.model.Employee;
import com.example.springbackend.readingwriting.CsvService;
import com.example.springbackend.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CsvService csvService;

    public EmployeeService(EmployeeRepository employeeRepository, CsvService csvService) {
        this.employeeRepository = employeeRepository;
        this.csvService = csvService;
    }

    public Page<Employee> getEmployees(int page, int pageSize, String sortBy, Sort.Direction direction) {
        return employeeRepository.findAll(PageRequest.of(page, pageSize, Sort.by(direction, sortBy)));
    }

    public Employee getEmployeeById(Long id) {
        return findEmployeeById(id);
    }

    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException(id));
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

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
