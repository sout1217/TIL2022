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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime expireDate;

    private String role;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
