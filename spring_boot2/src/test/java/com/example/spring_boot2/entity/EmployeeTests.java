package com.example.spring_boot2.entity;

import com.example.spring_boot2.repository.DepartmentRepository;
import com.example.spring_boot2.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void dataInsert() {
        Department salesTeam = Department.builder()
                .name("영업부")
                .build();
        Department businessTeam = Department.builder()
                .name("사업부")
                .build();
        Department developTeam = Department.builder()
                .name("개발부")
                .build();

        Employee martin = Employee.builder()
                .name("martin")
                .dept(developTeam)
                .build();
        Employee tom = Employee.builder()
                .name("tom")
                .dept(businessTeam)
                .build();
        Employee benny = Employee.builder()
                .name("benny")
                .build();


        departmentRepository.save(salesTeam);
        departmentRepository.save(businessTeam);
        departmentRepository.save(developTeam);

        employeeRepository.save(martin);
        employeeRepository.save(tom);
        employeeRepository.save(benny);

    }

    @Test
    @DisplayName("엔티티 조회 테스트")
    void test1() {
        Employee employee = employeeRepository.findById(1L).orElse(null);

        assertThat(employee).isNotNull();
        assertThat(employee.getName()).isEqualTo("martin");


        Department department = departmentRepository.findById(1L).orElse(null);

        assertThat(department).isNotNull();
        assertThat(department.getName()).isEqualTo("영업부");

    }

    @Test
    @DisplayName("[즉시로딩 - EAGER Loading] 다대일 단방향 매핑 테스트")
    void test2() {
        Employee employee = employeeRepository.findById(1L).orElse(null);

        assertThat(employee).isNotNull();
        assertThat(employee.getName()).isEqualTo("martin");
        assertThat(employee.getDept().getName()).isEqualTo("개발부");

        Department department = departmentRepository.findById(3L).orElse(null);

        assertThat(department).isNotNull();
        assertThat(department.getName()).isEqualTo("개발부");

    }
}