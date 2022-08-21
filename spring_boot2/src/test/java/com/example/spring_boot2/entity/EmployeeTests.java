package com.example.spring_boot2.entity;

import com.example.spring_boot2.repository.DepartmentRepository;
import com.example.spring_boot2.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmployeeTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @PersistenceUnit
    private EntityManagerFactory emf;


    /**
     * 테스트를 실행하기 전, Department 클래스의 @OneToMany 의 cascade = CascadeType.PERSIST 옵션을 제거해야 한다
     */
    @Test
    @DisplayName("Cascade.Persist 를 활용하지 않은 경우 테스트")
    void test() {
        EntityManager em = emf.createEntityManager();

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
                .build();
        Employee tom = Employee.builder()
                .name("tom")
                .build();
        Employee benny = Employee.builder()
                .name("benny")
                .build();
        Employee kevin = Employee.builder()
                .name("kevin")
                .build();

        // 부서와 직원 연결하기
        businessTeam.addEmployee(tom);
        developTeam.addEmployee(martin);
        developTeam.addEmployee(kevin);

        departmentRepository.save(salesTeam);
        departmentRepository.save(businessTeam);
        departmentRepository.save(developTeam);

        employeeRepository.save(martin);
        employeeRepository.save(tom);
        employeeRepository.save(benny);
        employeeRepository.save(kevin);

        em.clear();

        Employee employee = em.find(Employee.class, 1L);
        Department department = em.find(Department.class, 3L);

        assertThat(employee.getName()).isEqualTo("martin");
        assertThat(employee.getDept().getName()).isEqualTo("개발부");

        assertThat(department.getName()).isEqualTo("개발부");

        List<Employee> employeeList = department.getEmployeeList();

        // lazy loading 이기 때문에 employee.getDept 처럼 되지 않는다
        assertThat(employeeList).hasSize(2);
        assertThat(employeeList)
                .extracting("name")
                .containsOnly("martin", "kevin");

        em.close();
    }

    /**
     * 테스트를 실행하기 전, Department 클래스의 @OneToMany 의 cascade = CascadeType.PERSIST 옵션을 설정해주어야 한다
     */
    @Test
    @DisplayName("Cascade.Persist 를 활용한 경우 테스트")
    void test2() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

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
                .dept(developTeam)
                .build();
        Employee kevin = Employee.builder()
                .name("kevin")
                .build();


        em.persist(salesTeam);
        em.persist(businessTeam);
        em.persist(developTeam);

        developTeam.addEmployee(martin);
        developTeam.addEmployee(tom);
        developTeam.addEmployee(benny);
        developTeam.addEmployee(kevin);
        tx.commit();
        em.clear();

        List<Employee> all = employeeRepository.findAll();

        assertThat(all).hasSize(4);

        em.close();
    }
}