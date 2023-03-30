package com.example.springbackend.readingwriting;

import com.example.springbackend.exception.ExportEmployeeToCsvException;
import com.example.springbackend.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CsvServiceTest {

    private CsvService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CsvService();
    }

    @Test
    void shouldExportEmployeesToCsv() {
        //given
        List<Employee> employeesToExport = getEmployees();
        //when
        byte[] result = underTest.exportEmployeesToCsv(employeesToExport);
        //then
        assertThat(result)
                .isNotNull();
    }

    @Test
    void shouldThrowExceptionDuringExportingToCsv() {
        //given
        List<Employee> givenEmployees = getEmployees();
        //when
        underTest = mock(CsvService.class);
        when(underTest.exportEmployeesToCsv(givenEmployees)).thenThrow(new ExportEmployeeToCsvException());
        //then
        assertThatThrownBy(() -> underTest.exportEmployeesToCsv(givenEmployees))
                .isInstanceOf(ExportEmployeeToCsvException.class)
                .hasMessage("Something went wrong during exporting employees to csv file");
    }

    @Test
    void shouldImportEmployeesFromCsv() {
        //given

        List<Employee> expected = getEmployees();
        MockMultipartFile testFile = new MockMultipartFile("TestFile", new byte[]{});
        //when
        underTest = mock(CsvService.class);
        when(underTest.importEmployees(testFile)).thenReturn(getEmployees());
        List<Employee> result = underTest.importEmployees(testFile);
        //then
        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(expected.size());
    }

    private static List<Employee> getEmployees() {
        return Arrays.asList(
                new Employee(1L, "Josh", "Clinton", "clinton@gmail.com", 2500.50),
                new Employee(2L, "Carl", "Bush", "bush@gmail.com", 2900.00),
                new Employee(3L, "Mark", "Jobs", "jobs@gmail.com", 2700.00)
        );
    }

}