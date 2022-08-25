package com.example.spring_boot2.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCard {

    @Id
    private Long id;

    private LocalDateTime expireDate;

    private String role;

    /**
     * 부모 테이블의 기본키를 자식 테이블에서 외래키와 동시에 자식 테이블의 기본키로 사용하는 경우가 있다
     * 즉, 참조키(fk)이면서 기본키(pk) 인 경우
     */
    @MapsId
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "emp_id")
    private Employee employee;
}
