package com.example.springbackend.service;

import com.example.springbackend.controller.request.EmployeeSearchCriteria;
import com.example.springbackend.exception.EmployeeNotFoundException;
import com.example.springbackend.model.Employee;
import com.example.springbackend.readingwriting.CsvService;
import com.example.springbackend.repository.EmployeeCriteriaRepository;
import com.example.springbackend.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeCriteriaRepository employeeCriteriaRepository;

    @Mock
    private CsvService csvService;

    @InjectMocks
    private EmployeeService underTest;

    @BeforeEach
    void setUp() {
        underTest = new EmployeeService(employeeRepository, employeeCriteriaRepository, csvService);
    }

    @Test
    void shouldGetEmployeePage() {
        //given
        EmployeeSearchCriteria givenSearchCriteria = new EmployeeSearchCriteria();
        givenSearchCriteria.setNumber(1);
        givenSearchCriteria.setSize(10);
        givenSearchCriteria.setSortBy("id");
        givenSearchCriteria.setSalaryGreaterEqual(2600.00);
        givenSearchCriteria.setDirection(Sort.Direction.ASC);

        List<Employee> expectedEmployeesOnPage = Arrays.asList(
                new Employee(),
                new Employee(),
                new Employee(),
                new Employee(),
                new Employee()
        );
        Pageable expectedPageable = PageRequest.of(givenSearchCriteria.getNumber(), givenSearchCriteria.getSize(),
                givenSearchCriteria.getDirection(), givenSearchCriteria.getSortBy());
        long expectedCount = givenSearchCriteria.getSize();
        Page<Employee> expectedPage = new PageImpl<>(expectedEmployeesOnPage, expectedPageable, expectedCount);
        //when
        when(employeeCriteriaRepository.getEmployeePageWithCriteria(givenSearchCriteria)).thenReturn(expectedPage);
        Page<Employee> result = underTest.getEmployees(givenSearchCriteria);

        //then
        assertThat(result)
                .isNotNull()
                .isEqualTo(expectedPage);

        assertThat(result.getContent())
                .isNotNull()
                .isEqualTo(expectedEmployeesOnPage)
                .hasSize(expectedEmployeesOnPage.size());

        assertThat(result.getPageable())
                .isNotNull()
                .isEqualTo(expectedPageable);

        assertThat(result.getSize())
                .isNotNull()
                .isEqualTo(expectedCount);

        verify(employeeCriteriaRepository, times(1)).getEmployeePageWithCriteria(givenSearchCriteria);
    }

    @Test
    void shouldImportEmployees() {
        //given
        List<Employee> employeesToSave = Arrays.asList(
                new Employee(),
                new Employee(),
                new Employee(),
                new Employee(),
                new Employee()
        );
        MockMultipartFile testFile = new MockMultipartFile("testFile", new byte[]{});

        //when
        when(csvService.importEmployees(testFile)).thenReturn(employeesToSave);
        underTest.importEmployees(testFile);

        //then
        verify(employeeRepository, times(1)).saveAll(employeesToSave);
    }

    @Test
    void shouldExportEmployees() {
        //given
        List<Employee> employeesToExport = Arrays.asList(
                new Employee(),
                new Employee(),
                new Employee(),
                new Employee(),
                new Employee()
        );
        String exportType = "csv";
        byte[] testReturnArray = {};
        //when
        when(employeeRepository.findAll()).thenReturn(employeesToExport);
        when(csvService.exportEmployeesToCsv(employeesToExport)).thenReturn(testReturnArray);

        byte[] result = underTest.exportEmployees(exportType);
        //then
        assertThat(result)
                .isNotNull()
                .isEqualTo(testReturnArray);
        verify(employeeRepository, times(1)).findAll();
    }

    @ParameterizedTest(name = "Params: employeeId=`{0}`")
    @MethodSource("shouldThrowExceptionIfEmployeeNotFoundParams")
    void shouldThrowExceptionIfEditingEmployeeNotFound(long employeeId) {
        //given
        Employee editFrom = new Employee();
        editFrom.setEmail("newEmail@gmail.com");
        //when
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> underTest.updateEmployeeById(employeeId, editFrom))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage(String.format("Employee with id: %d not found", employeeId));
    }

    @ParameterizedTest(name = "Params: employeeId=`{0}`")
    @MethodSource("shouldThrowExceptionIfEmployeeNotFoundParams")
    void shouldEditExistingEmployeeById(long employeeId) {
        //given
        Employee toUpdate = new Employee();
        toUpdate.setEmail("oldEmail@gmail.com");

        Employee editFrom = new Employee();
        editFrom.setFirstName("Carl");
        editFrom.setLastName("Bush");
        editFrom.setEmail("newEmail@gmail.com");
        editFrom.setSalary(2950.50);
        //when
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(toUpdate));
        when(employeeRepository.save(toUpdate)).thenReturn(toUpdate);

        Employee editedEmployee = underTest.updateEmployeeById(employeeId, editFrom);
        //then
        assertThat(editedEmployee.getFirstName()).isEqualTo(editFrom.getFirstName());
        verify(employeeRepository, times(1)).save(toUpdate);
    }

    @ParameterizedTest(name = "Params: employeeId=`{0}`")
    @MethodSource("shouldThrowExceptionIfEmployeeNotFoundParams")
    void shouldThrowExceptionIfDeletingEmployeeNotFound(long employeeId) {
        //when
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> underTest.deleteEmployeeById(employeeId))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage(String.format("Employee with id: %d not found", employeeId));

    }

    @ParameterizedTest(name = "Params: employeeId=`{0}`")
    @MethodSource("shouldFindExistingEmployeeByIdParams")
    void shouldDeleteExistingEmployeeById(long employeeId) {
        //given
        Employee givenEmployee = new Employee();
        givenEmployee.setId(employeeId);
        //when
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(givenEmployee));
        underTest.deleteEmployeeById(employeeId);
        //then
        verify(employeeRepository, times(1)).delete(givenEmployee);
    }


    @ParameterizedTest(name = "Params: employeeId=`{0}`")
    @MethodSource("shouldFindExistingEmployeeByIdParams")
    void shouldFindExistingEmployeeById(long employeeId) {
        //given
        Employee givenEmployee = new Employee();
        givenEmployee.setId(employeeId);
        //when
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(givenEmployee));
        //then
        assertThat(underTest.getEmployeeById(employeeId)).isEqualTo(givenEmployee);
    }

    @ParameterizedTest(name = "Params: employeeId=`{0}`")
    @MethodSource("shouldThrowExceptionIfEmployeeNotFoundParams")
    void shouldThrowExceptionIfEmployeeNotFound(long employeeId) {
        //when
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() ->
                underTest.getEmployeeById(employeeId))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage(String.format("Employee with id: %d not found", employeeId));
    }


    private static Stream<Arguments> shouldFindExistingEmployeeByIdParams() {
        return Stream.of(
                arguments(1),
                arguments(2),
                arguments(3)
        );
    }

    private static Stream<Arguments> shouldThrowExceptionIfEmployeeNotFoundParams() {
        return Stream.of(
                arguments(-1),
                arguments(0),
                arguments(1)
        );
    }

}
