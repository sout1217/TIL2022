package com.example.spring_boot2.entity;

import com.example.spring_boot2.repository.EmployeeCardRepository;
import com.example.spring_boot2.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmployeeTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeCardRepository employeeCardRepository;


    @PersistenceUnit
    private EntityManagerFactory emf;

    @BeforeEach
    void dataInsert() {

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

        EmployeeCard martinCard = EmployeeCard.builder()
                .employee(martin)
                .expireDate(LocalDateTime.now())
                .role("ROLE_USER")
                .build();
        EmployeeCard tomCard = EmployeeCard.builder()
                .employee(tom)
                .expireDate(LocalDateTime.now())
                .role("ROLE_USER")
                .build();
        EmployeeCard bennyCard = EmployeeCard.builder()
                .employee(benny)
                .expireDate(LocalDateTime.now())
                .role("ROLE_USER")
                .build();
        EmployeeCard kevinCard = EmployeeCard.builder()
                .employee(kevin)
                .expireDate(LocalDateTime.now())
                .role("ROLE_USER")
                .build();

        // cascade.PERSIST 로 emp 도 같이 저장되도록 함
        employeeCardRepository.save(martinCard);
        employeeCardRepository.save(tomCard);
        employeeCardRepository.save(bennyCard);
        employeeCardRepository.save(kevinCard);

        /**
         * EmployeeCard Table
         * [이론적인 컬럼] id, expire_date, role, emp_id
         * [실제 저장되는 컬럼] emp_id, expire_date, role
         * @JoinColumn 으로 참조키를 명시하고,
         * @MapsId 로 참조키 이면서, 기본키다라고 명시하였다.
         * @Id id 가 적혀있음에도 실제 테이블에는 EmployeeCard id 컬럼은 존재하지 않는다.
         *
         * Employee Table
         * id, name
         */

    }

    /**
     *  영속성 컨텍스트에서 조회한 경우에는 같은 인스턴스를 반환한다 (sql mapper 는 new instance 로 == 비교가 안된다)
     */
    @Test
    @DisplayName("@MapsId Employee employee 필드 값 설정 시, employee.id 값이 employeeCard.id 값으로 들어가진다")
    void test() {
        EntityManager em = emf.createEntityManager();

        Employee employee = em.find(Employee.class, 1L);
        EmployeeCard employeeCard = em.find(EmployeeCard.class, employee.getId());

        assertThat(employeeCard.getId()).isEqualTo(employee.getId());
        assertThat(employeeCard.getId()).isEqualTo(employeeCard.getEmployee().getId());
        assertThat(employee == employeeCard.getEmployee()).isTrue();
        assertThat(employeeCard.getEmployee().getName()).isEqualTo("martin");

        em.close();
    }

}