package com.example.crud.employee;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class Employee {

    private @Id @GeneratedValue Long id;
    private final String name;
    private final String position;
    private final Integer salary;

    protected Employee(){
        this(null, null, null);
    }
}
