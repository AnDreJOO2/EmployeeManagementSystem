package com.example.springbackend.readingwriting;

import com.example.springbackend.model.Employee;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Service
public class CsvService {

    public byte[] exportEmployeesToCsv(List<Employee> collection) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(output), format)) {

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
            throw new RuntimeException(ex.getMessage());
        }
    }
}
