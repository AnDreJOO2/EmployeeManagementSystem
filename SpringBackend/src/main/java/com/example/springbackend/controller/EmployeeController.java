package com.example.springbackend.controller;

import com.example.springbackend.model.Employee;
import com.example.springbackend.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/employees")
@CrossOrigin(origins = "*", maxAge = 3600L)
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping()
    public ResponseEntity<Page<Employee>>
    getEmployees(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                 @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
                 @RequestParam(value = "direction", required = false, defaultValue = "ASC") Sort.Direction direction) {

        page = page < 1 ? 1 : page;
        pageSize = pageSize < 1 ? 1 : pageSize;
        return new ResponseEntity<>(employeeService.getEmployees(page - 1, pageSize, sortBy, direction), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.addEmployee(employee), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable("id") Long id, @RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.updateEmployeeById(id, employee), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable("id") Long id) {
        employeeService.deleteEmployeeById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
