package com.example.spring_boot2.repository;


import com.example.spring_boot2.entity.Employee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Spring Data JPA - Like 쿼리
     * https://recordsoflife.tistory.com/59
     * Containing = Contains = IsContaining : 파라미터에 와일드 카드 %% 를 안붙여도 된다
     * Like : 파라미터에 와일드 카드 %% 을 붙여야 한다
     * StartWith : %키워드 로 조회한다
     * EndWith : 키워드% 로 조회한다
     */
    List<Employee> findAllByNameContainsOrderByNameDesc(String name);
    List<Employee> findAllByNameContains(String name, Sort sort);
}
