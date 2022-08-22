package com.example.spring_boot2.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * EAGER : 즉시 로딩
     * optional : [기본값] true -> left outer join | false -> inner join
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Department dept;

    /** 연결관게 편의 메소드 */
    public void refDept(Department dept) {
        this.dept = dept;
        dept.getEmployeeList().add(this);
    }

    /** 연결관계 끊기 **/
    public void standbyDepartment() {
        this.dept = null;
    }
}
