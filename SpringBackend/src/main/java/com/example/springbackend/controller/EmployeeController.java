package com.example.springbackend.controller;

import com.example.springbackend.controller.request.EmployeeSearchCriteria;
import com.example.springbackend.exception.IllegalExportTypeException;
import com.example.springbackend.exception.IllegalFileExtensionException;
import com.example.springbackend.exception.ImportNullFileException;
import com.example.springbackend.model.Employee;
import com.example.springbackend.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("api/employees")
@CrossOrigin(origins = "*", maxAge = 3600L)
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping()
    public ResponseEntity<Page<Employee>> getEmployees(
            @RequestParam(required = false, name = "number") Integer number,
            @RequestParam(required = false, name = "size") Integer size,
            @RequestParam(required = false, name = "sortBy") String sortBy,
            @RequestParam(required = false, name = "direction") String direction,
            @RequestParam(required = false, name = "firstNameLike") String firstNameLike,
            @RequestParam(required = false, name = "lastNameLike") String lastNameLike,
            @RequestParam(required = false, name = "emailLike") String emailLike,
            @RequestParam(required = false, name = "salaryGreaterEqual") Double salaryGreaterEqual,
            @RequestParam(required = false, name = "salaryLessEqual") Double salaryLessEqual) {

        EmployeeSearchCriteria employeeSearchCriteria = new EmployeeSearchCriteria();
        buildEmployeeSearchCriteria(employeeSearchCriteria, number, size, sortBy, direction, firstNameLike, lastNameLike,
                emailLike, salaryGreaterEqual, salaryLessEqual);
        return new ResponseEntity<>(employeeService.getEmployees(employeeSearchCriteria), HttpStatus.OK);
    }

    @GetMapping("export")
    public ResponseEntity<byte[]> exportEmployees(@RequestParam(required = false, defaultValue = "csv") String type) {
        type = type.toLowerCase();
        if (!type.equals("csv")) {
            throw new IllegalExportTypeException(type);
        }
        String fileName = "employees." + type;

        byte[] file = employeeService.exportEmployees(type);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/" + type))
                .body(file);
    }

    @PostMapping("import")
    public ResponseEntity<?> importEmployees(@RequestParam MultipartFile file) {
        if (Objects.isNull(file)) {
            throw new ImportNullFileException();
        }
        if (!file.getOriginalFilename().endsWith(".csv")) {
            throw new IllegalFileExtensionException(file.getOriginalFilename());
        }
        employeeService.importEmployees(file);
        return new ResponseEntity<>(HttpStatus.OK);
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

    private void buildEmployeeSearchCriteria(EmployeeSearchCriteria employeeSearchCriteria, Integer number, Integer size, String sortBy, String direction,
                                             String firstNameLike, String lastNameLike, String emailLike, Double salaryGreaterEqual, Double salaryLessEqual) {
        if (Objects.nonNull(number)) {
            employeeSearchCriteria.setNumber(number);
        }
        if (Objects.nonNull(size)) {
            employeeSearchCriteria.setSize(size);
        }
        if (Objects.nonNull(sortBy)) {
            employeeSearchCriteria.setSortBy(sortBy);
        }
        if (Objects.nonNull(direction)) {
            if (direction.equals("ASC")) {
                employeeSearchCriteria.setDirection(Sort.Direction.ASC);
            } else {
                employeeSearchCriteria.setDirection(Sort.Direction.DESC);
            }
        }
        if (Objects.nonNull(firstNameLike)) {
            employeeSearchCriteria.setFirstNameLike(firstNameLike);
        }
        if (Objects.nonNull(lastNameLike)) {
            employeeSearchCriteria.setLastNameLike(lastNameLike);
        }
        if (Objects.nonNull(emailLike)) {
            employeeSearchCriteria.setEmailLike(emailLike);
        }
        if (Objects.nonNull(salaryGreaterEqual)) {
            employeeSearchCriteria.setSalaryGreaterEqual(salaryGreaterEqual);
        }
        if (Objects.nonNull(salaryLessEqual)) {
            employeeSearchCriteria.setSalaryLessEqual(salaryLessEqual);
        }

    }
}
