package com.example.spring_boot2.entity;

import com.example.spring_boot2.repository.EmployeeCardRepository;
import com.example.spring_boot2.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
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

        EmployeeCard martinCard = EmployeeCard.builder()
                .expireDate(LocalDateTime.now())
                .role("ROLE_USER")
                .build();
        EmployeeCard tomCard = EmployeeCard.builder()
                .expireDate(LocalDateTime.now())
                .role("ROLE_USER")
                .build();
        EmployeeCard bennyCard = EmployeeCard.builder()
                .expireDate(LocalDateTime.now())
                .role("ROLE_USER")
                .build();
        EmployeeCard kevinCard = EmployeeCard.builder()
                .expireDate(LocalDateTime.now())
                .role("ROLE_USER")
                .build();


        Employee martin = Employee.builder()
                .employeeCard(martinCard)
                .name("martin")
                .build();
        Employee tom = Employee.builder()
                .name("tom")
                .employeeCard(tomCard)
                .build();
        Employee benny = Employee.builder()
                .name("benny")
                .employeeCard(bennyCard)
                .build();
        Employee kevin = Employee.builder()
                .employeeCard(kevinCard)
                .name("kevin")
                .build();

        /**
         * 저장순서가 중요하다
         */
        employeeCardRepository.save(martinCard);
        employeeCardRepository.save(tomCard);
        employeeCardRepository.save(bennyCard);
        employeeCardRepository.save(kevinCard);

        employeeRepository.save(martin);
        employeeRepository.save(tom);
        employeeRepository.save(benny);
        employeeRepository.save(kevin);


    }

    @Test
    void test() {
        EntityManager em = emf.createEntityManager();

        Employee employee = em.find(Employee.class, 1L);

        assertThat(employee.getName()).isEqualTo("martin");
        assertThat(employee.getEmployeeCard().getRole()).isEqualTo("ROLE_USER");
        assertThat(employee.getEmployeeCard().getExpireDate()).isBefore(LocalDateTime.of(2022, 8, 23, 23, 0, 0));

        /**
         * LocalDateTime 의 테스트 실패 시, 아래와 같이 나온다 (나중에 테스트 하는 경우 isBefore -> isAfter 로 변경바람)
         * java.lang.AssertionError:
         * Expecting actual:
         *   2022-08-23T20:51:09.357760 (java.time.LocalDateTime)
         * to be strictly before:
         *   2022-05-23T23:00 (java.time.LocalDateTime)
         * when comparing values using 'ChronoLocalDateTime.timeLineOrder()'
         */

        em.close();
    }

}