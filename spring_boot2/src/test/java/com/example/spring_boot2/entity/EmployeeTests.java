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
                .employee(martin)
                .expireDate(LocalDateTime.now())
                .role("ROLE_USER")
                .build();
        EmployeeCard bennyCard = EmployeeCard.builder()
                .employee(tom)
                .expireDate(LocalDateTime.now())
                .role("ROLE_USER")
                .build();
        EmployeeCard kevinCard = EmployeeCard.builder()
                .employee(martin)
                .expireDate(LocalDateTime.now())
                .role("ROLE_USER")
                .build();




        /**
         * 저장순서가 중요하다
         */
        employeeRepository.save(martin);
        employeeRepository.save(tom);
        employeeRepository.save(benny);
        employeeRepository.save(kevin);

        employeeCardRepository.save(martinCard);
        employeeCardRepository.save(tomCard);
        employeeCardRepository.save(bennyCard);
        employeeCardRepository.save(kevinCard);

    }

    @Test
    void test() {
        EntityManager em = emf.createEntityManager();

        EmployeeCard employeeCard1 = em.find(EmployeeCard.class, 1L);
        EmployeeCard employeeCard2 = em.find(EmployeeCard.class, 2L);
        EmployeeCard employeeCard3 = em.find(EmployeeCard.class, 4L);

        assertThat(employeeCard1.getEmployee().getName()).isEqualTo("martin");
        assertThat(employeeCard2.getEmployee().getName()).isEqualTo("martin");
        assertThat(employeeCard3.getEmployee().getName()).isEqualTo("martin");

        em.close();
    }

}