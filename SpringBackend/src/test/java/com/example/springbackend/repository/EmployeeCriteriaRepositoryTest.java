package com.example.springbackend.repository;

import com.example.springbackend.controller.request.EmployeeSearchCriteria;
import com.example.springbackend.model.Employee;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
class EmployeeCriteriaRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    private EmployeeCriteriaRepository underTest;

    @BeforeEach
    void setUp() {
        underTest = new EmployeeCriteriaRepository(entityManager);
    }

    @ParameterizedTest(name = "Params: criteria={0}")
    @MethodSource("shouldGetEmployeePageWithCriteriaParams")
    void shouldGetEmployeePageWithCriteria(EmployeeSearchCriteria employeeSearchCriteria) {
        //when
        Page<Employee> result = underTest.getEmployeePageWithCriteria(employeeSearchCriteria);
        //then
        assertThat(result).isNotNull();
        assertThat(result.getTotalPages()).isGreaterThanOrEqualTo(1);
        assertThat(result.getTotalElements()).isGreaterThanOrEqualTo(1);
    }

    static Stream<Arguments> shouldGetEmployeePageWithCriteriaParams() {
        EmployeeSearchCriteria criteria1 = new EmployeeSearchCriteria();
        criteria1.setFirstNameOrLastNameOrEmailLike("Ev");
        criteria1.setSalaryLessEqual(2900.0);
        criteria1.setSortBy("salary");
        criteria1.setDirection(Sort.Direction.ASC);

        EmployeeSearchCriteria criteria2 = new EmployeeSearchCriteria();
        criteria2.setFirstNameOrLastNameOrEmailLike("George");
        criteria2.setSalaryLessEqual(2900.0);
        criteria2.setSortBy("lastName");
        criteria2.setDirection(Sort.Direction.DESC);

        EmployeeSearchCriteria criteria3 = new EmployeeSearchCriteria();
        criteria3.setFirstNameOrLastNameOrEmailLike("Evelyn T");
        criteria3.setSalaryGreaterEqual(2500.0);
        criteria3.setSortBy("email");

        return Stream.of(
                arguments(criteria1),
                arguments(criteria2),
                arguments(criteria3)
        );
    }

}
