package com.example.spring_boot2.repository;


import com.example.spring_boot2.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {


}
