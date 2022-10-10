package com.example.spring_boot2.controller;

import com.example.spring_boot2.entity.Employee;
import com.example.spring_boot2.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmployeeControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("정렬 조회")
    void test1() throws Exception {

        // given
        List<Employee> employeeList = List.of(
                Employee.builder()
                        .name("martin")
                        .build(),
                Employee.builder()
                        .name("benny")
                        .build(),
                Employee.builder()
                        .name("tom-martin")
                        .build()
        );
        employeeRepository.saveAll(employeeList);

        // when
        ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("/employees")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "martin")
                        .param("sortType", "springDataJpa")
        );

        // then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", hasSize(2)))
        ;

    }
    @Test
    @DisplayName("정렬 조회2")
    void test2() throws Exception {

        // given
        List<Employee> employeeList = List.of(
                Employee.builder()
                        .name("martin")
                        .build(),
                Employee.builder()
                        .name("benny")
                        .build(),
                Employee.builder()
                        .name("tom-martin")
                        .build()
        );
        employeeRepository.saveAll(employeeList);

        // when
        ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("/employees")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "martin")
                        .param("sortType", "sortClass")
        );

        // then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", hasSize(2)))
        ;

    }


}