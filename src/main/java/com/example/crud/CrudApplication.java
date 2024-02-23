package com.example.crud;

import com.example.crud.employee.Employee;
import com.example.crud.employee.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@SpringBootApplication
public class CrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudApplication.class, args);
    }

    @Autowired
    EmployeeRepository employeeRepository;

    @PostConstruct
    void populate(){
        employeeRepository.save(new Employee("John", "pos1", 123));
        employeeRepository.save(new Employee("Bob", "pos3", 999));
    }
}
