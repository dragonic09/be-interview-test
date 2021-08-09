package com.backend.interviewtest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto {
    Long employeeId;
    String firstName;
    String lastName;
    String department;
}
