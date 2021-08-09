package com.backend.interviewtest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeListResponseDto extends ResponseTemplateDto {
    List<EmployeeDto> employees;
}
