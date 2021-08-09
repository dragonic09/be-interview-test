package com.backend.interviewtest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponseDto extends ResponseTemplateDto {
    EmployeeDto employee;
}
