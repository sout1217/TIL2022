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
                .employeeCard(martinCard)
                .name("tom")
                .build();
        Employee benny = Employee.builder()
                .employeeCard(tomCard)
                .name("benny")
                .build();
        Employee kevin = Employee.builder()
                .employeeCard(martinCard)
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

        Employee martin = em.find(Employee.class, 1L);
        Employee tom = em.find(Employee.class, 2L);

        assertThat(martin.getName()).isEqualTo("martin");
        assertThat(martin.getEmployeeCard().getId()).isEqualTo(1L);
        assertThat(tom.getEmployeeCard().getId()).isEqualTo(1L);

        em.close();
    }

}