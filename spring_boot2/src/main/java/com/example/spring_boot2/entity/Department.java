package com.example.spring_boot2.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@ToString(exclude = "employeeList")
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * mappedBy : 관계의 주인을 명시하기 위해 사용하며, 관계의 주인을 Emplotee.dept 로 명시한다
     *            주인은 @JoinColumn(참조키) 를 사용하며, 주인이 아니면 매핑어노테이션에 mappedBy 를 설정한다.
     *            데이터 모델링에서 부서 > 직원으로
     *            부서(department)가 부모 테이블, 직원(employee)은 자식 테이블로,
     *            비즈니스 로직이 변경 될 때, 자식 테이블이 참조키를 갖고있어야 변경이 쉬워서,
     *            참조키는 직원(employee)이 갖고 있는다.
     *
     * LAZY : 지연로딩
     *        모든 직원(employee)을 부서(department)를 조회할 때마다, 조회 한다면, 성능상 이슈가 있기 때문에
     *        직원 데이터가 필요할 때만 조회하기 위해 지연로딩을 사용한다
     *
     * orphanRemoval : [기본값] false
     *                 true 인 경우, 반드시 employee 가 존재해야 한다 (not null)
     *
     * CascadeType.PERSIST : employeeList 에 employee 가 추가 될 때, em.persist(Employee) 를 하지 않아도 자동으로 해줄 수 있다
     */
    @Builder.Default
    @OneToMany(mappedBy = "dept", fetch = FetchType.LAZY, orphanRemoval = false, cascade = CascadeType.PERSIST)
    private List<Employee> employeeList = new ArrayList<>();

    /** 연결관계 편의 메소드 */
    public void addEmployee(Employee emp) {
        this.employeeList.add(emp);
        emp.refDept(this);
    }
}

