package com.example.spring_boot2.entity;

import com.example.spring_boot2.repository.DepartmentRepository;
import com.example.spring_boot2.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmployeeTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @PersistenceUnit
    private EntityManagerFactory emf;

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
                .dept(developTeam)
                .build();
        Employee kevin = Employee.builder()
                .name("kevin")
                .build();



        businessTeam.getEmployeeList().add(tom);
        developTeam.getEmployeeList().add(martin);
        developTeam.getEmployeeList().add(kevin);

        departmentRepository.save(salesTeam);
        departmentRepository.save(businessTeam);
        departmentRepository.save(developTeam);

        employeeRepository.save(martin);
        employeeRepository.save(tom);
        employeeRepository.save(benny);

    }

    @Test
    @DisplayName("다대일 일대다 양방향 매핑 테스트")
    void test1() {
        /*
         * lazy loading 의 경우 osiv(open session in view) 가 false 인 경우 transaction 안에서 사용가능하기 때문에
         * test 코드에서 작성 시 lazy initialize exception 이 발생한다
         * [3가지 해결방법]
         * 1. test1() 메서드에 @Transactional 을 감싼다
         * 2. 해당 클래스에 @Transactional 로 감싼다
         * 3. emf 를 통해서 새로운 EntityManager 를 생성해서 한다
         *
         * 조회의 경우 tx.begin() / tx.commit() / tx.rollback() 을 사용하지 않아도 된다다         */
        EntityManager em = emf.createEntityManager();


        // emp 에서 dept 조회
        Employee employee = em.find(Employee.class, 1L);

        assertThat(employee).isNotNull();
        assertThat(employee.getName()).isEqualTo("martin");
        assertThat(employee.getDept().getName()).isEqualTo("개발부");

        // dept 에서 emp 조회
        Department department = em.find(Department.class, 3L);

        assertThat(department).isNotNull();
        assertThat(department.getEmployeeList().get(0).getName()).isEqualTo("martin");
        assertThat(department.getEmployeeList().get(1).getName()).isEqualTo("benny");

        em.close();
    }


    @Test
    @DisplayName("다대일 일대다 양방향 매핑 - em persist, dirty checking, lazy loading")
    void test2() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        Department aaaTeam = Department.builder()
                .name("A_Team")
                .build();
        em.persist(aaaTeam);

        Employee apple = Employee.builder()
                .name("apple")
                .dept(aaaTeam)
                .build();
        Employee melon = Employee.builder()
                .name("melon")
                .dept(aaaTeam)
                .build();
        em.persist(apple);
        em.persist(melon);

        // 연결관계 편의 메소드를 사용하지 않는 경우
        aaaTeam.getEmployeeList().add(apple);
        aaaTeam.getEmployeeList().add(melon);

        tx.commit();
        em.clear(); // 영속성 컨텍스트 비우기

        Department department = em.find(Department.class, 4L);
        assertThat(department.getEmployeeList()).hasSize(2);
        assertThat(department.getEmployeeList().get(0).getName()).isEqualTo("apple");
        assertThat(department.getEmployeeList().get(1).getName()).isEqualTo("melon");

        em.close();
        /*
         * 영속성이 살아있는 동안 트랜잭션도 살아있다. 그래서 emf 에서는 create 하는 메소드명인데, em 에서는 getTransaction 가져오는 명으로 되어있는 것 이다,
         * em.close() 로 닫히면, 트랜잭션도 닫히기 때문에, 만약 lazy loading 을 close() 문 밑에 작성한다면,
         * osiv 가 false 일 때 lazy loadingd 은 transaction 안에서만 사용할 수 있기 때문에, Lazy Initialize Exception 이 발생한다
         */
    }

    @Test
    @DisplayName("다대일 일대다 양방향 매핑 - [연결관계 편의 메소드 이용] em persist, dirty checking, lazy loading ")
    void test3() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        Department aaaTeam = Department.builder()
                .name("A_Team")
                .build();
        em.persist(aaaTeam);

        // emp 생성자 builder 에서 dept 참조를 하지 않음
        Employee apple = Employee.builder()
                .name("apple")
                .build();
        Employee melon = Employee.builder()
                .name("melon")
                .build();
        em.persist(apple);
        em.persist(melon);

        // 연결관계 편의 메소드를 사용하지 않는 경우 (emp 생성자에서 dept 참조 코드가 제거됨)
        // 양방향 emp-dept 참조
        apple.refDept(aaaTeam);
        melon.refDept(aaaTeam);

        tx.commit();
        em.clear(); // 영속성 컨텍스트 비우기

        Department department = em.find(Department.class, 4L);
        assertThat(department.getEmployeeList()).hasSize(2);
        assertThat(department.getEmployeeList().get(0).getName()).isEqualTo("apple");
        assertThat(department.getEmployeeList().get(1).getName()).isEqualTo("melon");

        em.close();
    }
}