package com.example.springbackend.controller;

import com.example.springbackend.controller.advice.GeneralControllerAdvice;
import com.example.springbackend.exception.IllegalExportTypeException;
import com.example.springbackend.exception.IllegalFileExtensionException;
import com.example.springbackend.model.Employee;
import com.example.springbackend.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController underTest;


    @BeforeEach
    void setUp() {
        underTest = new EmployeeController(employeeService);

        mockMvc = MockMvcBuilders.standaloneSetup(underTest)
                .setControllerAdvice(new GeneralControllerAdvice())
                .build();
    }

    @ParameterizedTest(name = "Params: params={0}")
    @MethodSource("shouldGetEmployeesParams")
    void shouldGetEmployees(MultiValueMap<String, String> params) throws Exception {
        //when
        when(employeeService.getEmployees(any())).thenReturn(any());
        //then
        MvcResult mvcResult = mockMvc.perform(get("/api/employees")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void shouldExportEmployeesThrowException() throws Exception {
        //given
        String type = "aaa";
        //when
        //then
        mockMvc.perform(get("/api/employees/export")
                        .param("type", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IllegalExportTypeException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .containsSequence(String.format("Illegal export type: %s ", type)));
    }

    @Test
    public void shouldExportEmployees() throws Exception {
        //given
        String type = "csv";
        String fileName = "employees." + type;
        //when
        //then
        MvcResult result = mockMvc.perform(get("/api/employees/export").param("type", type)
                        .contentType(MediaType.parseMediaType("application/" + type))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName))
                .andExpect(status().isOk())
                .andReturn();

        verify(employeeService, times(1)).exportEmployees(any());
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void shouldImportEmployeesThrowExceptionIfIllegalFileExtension() throws Exception {
        //given
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "employees.aaa",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/employees/import")
                        .file(file)
                        .param("file", String.valueOf(file)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IllegalFileExtensionException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .containsSequence(String.format("File: %s has illegal extension", file.getOriginalFilename())));
    }

    @Test
    public void shouldImportEmployees() throws Exception {
        //given
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "employees.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        //when
        //then
        MvcResult result = mockMvc.perform(multipart("/api/employees/import")
                        .file(file)
                        .param("file", String.valueOf(file)))
                .andExpect(status().isOk())
                .andReturn();

        verify(employeeService, times(1)).importEmployees(any());
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void shouldGetEmployeeById() throws Exception {
        //when
        when(employeeService.getEmployeeById(any())).thenReturn(any());
        //then
        MvcResult mvcResult = mockMvc.perform(get("/api/employees/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        verify(employeeService, times(1)).getEmployeeById(any());
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void shouldAddEmployee() throws Exception {

        //given
        Employee toAdd = new Employee();
        toAdd.setFirstName("Mike");
        toAdd.setLastName("Carlson");
        toAdd.setEmail("mike@gmail.com");
        toAdd.setSalary(2500.0);
        //when
        when(employeeService.addEmployee(any())).thenReturn(toAdd);
        //then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(toAdd));

        MvcResult result = mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andReturn();

        verify(employeeService, times(1)).addEmployee(any());
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void shouldUpdateEmployeeById() throws Exception {
        //given
        Employee toEdit = new Employee();
        toEdit.setFirstName("Mike");
        toEdit.setLastName("Carlson");
        toEdit.setEmail("mike@gmail.com");
        toEdit.setSalary(2500.0);

        //when
        when(employeeService.updateEmployeeById(Mockito.any(), Mockito.any())).thenReturn(toEdit);

        //then
        String json = new ObjectMapper().writeValueAsString(toEdit);
        MvcResult result = mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(employeeService, times(1)).updateEmployeeById(any(), any());
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
    }

    @Test
    public void shouldDeleteEmployeeById() throws Exception {
        //then
        MvcResult result = mockMvc.perform(delete("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(employeeService, times(1)).deleteEmployeeById(any());
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    static Stream<Arguments> shouldGetEmployeesParams() {
        MultiValueMap<String, String> map1 = new LinkedMultiValueMap<>();
        map1.put("number", List.of(String.valueOf(2)));
        map1.put("size", List.of(String.valueOf(2)));
        map1.put("sortBy", List.of("id"));
        map1.put("direction", List.of("ASC"));
        map1.put("firstNameOrLastNameOrEmailLike", List.of("Geo"));
        map1.put("salaryGreaterEqual", List.of(String.valueOf(2000)));
        map1.put("salaryLessEqual", List.of(String.valueOf(4000)));

        MultiValueMap<String, String> map2 = new LinkedMultiValueMap<>();
        map2.put("number", List.of(String.valueOf(3)));
        map2.put("size", List.of(String.valueOf(5)));
        map2.put("sortBy", List.of("email"));
        map2.put("direction", List.of("DESC"));
        map2.put("firstNameOrLastNameOrEmailLike", List.of("G B"));
        map2.put("salaryGreaterEqual", List.of(String.valueOf(2500)));
        map2.put("salaryLessEqual", List.of(String.valueOf(3500)));

        return Stream.of(
                arguments(map1),
                arguments(map2)
        );
    }

}
