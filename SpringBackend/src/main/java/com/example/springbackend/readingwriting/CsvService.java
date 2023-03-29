package com.example.springbackend.readingwriting;

import com.example.springbackend.exception.ExportEmployeeToCsvException;
import com.example.springbackend.exception.ImportEmployeeToCsvException;
import com.example.springbackend.model.Employee;
import org.apache.commons.csv.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CsvService {

    public static final String[] DEFAULT_HEADERS = {"id", "firstName", "lastName", "email", "salary"};

    public byte[] exportEmployeesToCsv(List<Employee> collection) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL)
                .withHeader(DEFAULT_HEADERS);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(output), format)) {
            csvPrinter.printRecord();
            for (Employee employee : collection) {
                List<String> record = Arrays.asList(
                        String.valueOf(employee.getId()),
                        String.valueOf(employee.getFirstName()),
                        String.valueOf(employee.getLastName()),
                        String.valueOf(employee.getEmail()),
                        String.valueOf(employee.getSalary())
                );
                csvPrinter.printRecord(record);
            }
            csvPrinter.flush();

            return output.toByteArray();
        } catch (IOException ex) {
            throw new ExportEmployeeToCsvException();
        }
    }

    public List<Employee> importEmployees(MultipartFile file) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), "UTF-8"));

            CSVFormat format = CSVFormat.DEFAULT.withHeader(DEFAULT_HEADERS).withSkipHeaderRecord();
            CSVParser csvParser = new CSVParser(br, format);
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            ArrayList<Employee> employees = new ArrayList<>();

            for (CSVRecord record : csvRecords
            ) {
                Employee employee = new Employee();

                String idFiled = record.get("id");
                if (idFiled != null && !idFiled.isEmpty()) {
                    employee.setId(Long.valueOf(idFiled));
                }

                String firstNameField = record.get("firstName");
                if (firstNameField != null && !firstNameField.isEmpty()) {
                    employee.setFirstName(firstNameField);
                }

                String lastNameField = record.get("lastName");
                if (lastNameField != null && !lastNameField.isEmpty()) {
                    employee.setLastName(lastNameField);
                }

                String emailField = record.get("email");
                if (emailField != null && !emailField.isEmpty()) {
                    employee.setEmail(emailField);
                }

                String salaryField = record.get("salary");
                if (salaryField != null && !salaryField.isEmpty()) {
                    employee.setSalary(Double.valueOf(salaryField));
                }
                employees.add(employee);
            }
            return employees;

        } catch (IOException e) {
            throw new ImportEmployeeToCsvException();
        }
    }
}
