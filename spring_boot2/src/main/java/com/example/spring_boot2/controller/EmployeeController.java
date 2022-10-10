package com.example.spring_boot2.controller;

import com.example.spring_boot2.dto.EmployeeDTO;
import com.example.spring_boot2.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    private ResponseEntity<?> getEntity(
            @RequestParam("name") String name,
            @RequestParam("sortType") SortType sortType
    ) {

        List<EmployeeDTO> employeeDTOS = null;

        if (sortType.equals(SortType.SPRING_DATA_JPA))
            employeeDTOS = employeeRepository.findAllByNameContainsOrderByNameDesc(name)
                    .stream().map(EmployeeDTO::from)
                    .collect(Collectors.toList());
        else if(sortType.equals(SortType.SORT_CLASS)) {
            Sort sort = Sort.by(Sort.Order.desc("name"));
            employeeDTOS = employeeRepository.findAllByNameContains(name, sort)
                    .stream().map(EmployeeDTO::from)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(employeeDTOS);
    }

}