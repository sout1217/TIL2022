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
                .build();
        Employee kevin = Employee.builder()
                .name("kevin")
                .build();

        martin.refDept(developTeam);
        kevin.refDept(developTeam);

        departmentRepository.save(salesTeam);
        departmentRepository.save(businessTeam);
        departmentRepository.save(developTeam);

        employeeRepository.save(martin);
        employeeRepository.save(tom);
        employeeRepository.save(benny);
        employeeRepository.save(kevin);
        /*
         * cascade 옵션 주의사항
         * kevin 저장을 맨 마지막 4번째에 했지만, 위에 kevin.refDept 에서 dept.getEmployeeList().add(this); 이부분에서 Cascade 옵션 Persist 작동으로 인해
         * kevin 은 2번째로 저장이 된다
         * employeeRepository.save(martin); 코드와 employeeRepository.save(kevin); 코드는 사실 없어도 제데로 동작한다
         */

    }


    @Test
    @DisplayName("연결관계 끊기 테스트")
    void test() {

        EntityManager em = emf.createEntityManager();

        Employee martin = em.find(Employee.class, 1L);
        Employee kevin = em.find(Employee.class, 2L);


        assertThat(martin.getName()).isEqualTo("martin");
        assertThat(kevin.getName()).isEqualTo("kevin");

        assertThat(martin.getDept().getName()).isEqualTo("개발부");
        assertThat(kevin.getDept().getName()).isEqualTo("개발부");

        em.getTransaction().begin();
        martin.standbyDepartment();
        em.getTransaction().commit();
        em.clear();

        Employee martin2 = em.find(Employee.class, 1L);
        Department department = em.find(Department.class, 3L);

        assertThat(martin2.getDept()).isNull();
        assertThat(department.getEmployeeList()).hasSize(1);

        em.close();
    }
}