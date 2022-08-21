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
     *            직원(Employee) 의 부서(Department)가 없는 경우도 조회해야 할 경우
     *            성능상 false(inner join) 을 하는 것이 유리
     *            하지만, @ManyToOne 의 경우 불러올 데이터가 dept 1개 밖에 되지 않기 때문에, EAGER 로딩으로 하면, sql 1개로 처리되기 때문에
     *            이점이 있어, 기본값이 EAGER 이다.
     *            반면 @OneToMany 는 Collection 형태로 여러개로 되어있는 경우, 자주 사용되지 않을 때, 성능 이슈가 있다
     *            비즈니스 로직에 맞춰, 해당 데이터를 자주 사용하는 지, 안사용하는 지에 따라 성능이슈 판단이 되기 때문에 확인하는 것이 좋다
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Department dept;
}
