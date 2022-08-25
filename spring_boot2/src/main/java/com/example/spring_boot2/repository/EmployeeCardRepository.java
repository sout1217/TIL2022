package com.example.spring_boot2.repository;

import com.example.spring_boot2.entity.EmployeeCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeCardRepository extends JpaRepository<EmployeeCard, Long> {
}
