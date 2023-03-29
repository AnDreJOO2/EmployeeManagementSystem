package com.example.springbackend.repository;

import com.example.springbackend.controller.request.EmployeeSearchCriteria;
import com.example.springbackend.model.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class EmployeeCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public EmployeeCriteriaRepository(EntityManager entityManager) {

        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Employee> getEmployeePageWithCriteria(EmployeeSearchCriteria employeeSearchCriteria) {

        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);
        Predicate predicate = getPredicate(root, employeeSearchCriteria);

        criteriaQuery.where(predicate);
        setOrder(employeeSearchCriteria, root, criteriaQuery);

        TypedQuery<Employee> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(employeeSearchCriteria.getNumber() * employeeSearchCriteria.getSize());
        typedQuery.setMaxResults(employeeSearchCriteria.getSize());

        Pageable pageable = getPageable(employeeSearchCriteria);

        long countEmployees = countEmployees();

        return new PageImpl<>(typedQuery.getResultList(), pageable, countEmployees);
    }

    private long countEmployees() {
        CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);
        Root<Employee> root = count.from(Employee.class);
        count.select(criteriaBuilder.count(root));
        return entityManager.createQuery(count).getSingleResult();
    }

    private void setOrder(EmployeeSearchCriteria employeeSearchCriteria, Root<Employee> root, CriteriaQuery<Employee> criteriaQuery) {
        if (employeeSearchCriteria.getDirection().isAscending()) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(employeeSearchCriteria.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(employeeSearchCriteria.getSortBy())));
        }
    }

    private Pageable getPageable(EmployeeSearchCriteria employeeSearchCriteria) {
        return PageRequest.of(employeeSearchCriteria.getNumber(), employeeSearchCriteria.getSize(),
                employeeSearchCriteria.getDirection(), employeeSearchCriteria.getSortBy());
    }

    private Predicate getPredicate(Root<Employee> root, EmployeeSearchCriteria employeeSearchCriteria) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(employeeSearchCriteria.getFirstNameLike())) {
            predicates.add(
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + employeeSearchCriteria.getFirstNameLike().toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + employeeSearchCriteria.getLastNameLike().toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + employeeSearchCriteria.getEmailLike().toLowerCase() + "%")
                    ));
        }

        if (Objects.nonNull(employeeSearchCriteria.getSalaryGreaterEqual())) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), employeeSearchCriteria.getSalaryGreaterEqual()));
        }

        if (Objects.nonNull(employeeSearchCriteria.getSalaryLessEqual())) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("salary"), employeeSearchCriteria.getSalaryLessEqual()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
