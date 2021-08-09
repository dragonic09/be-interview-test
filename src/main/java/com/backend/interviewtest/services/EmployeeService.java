package com.backend.interviewtest.services;

import com.backend.interviewtest.dto.EmployeeDto;
import com.backend.interviewtest.dto.ResponseTemplateDto;

import java.util.List;

public interface EmployeeService {
    EmployeeDto create(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto);
    List<EmployeeDto> getAllEmployees();
    EmployeeDto getEmployee(Long employeeId);
    EmployeeDto deleteEmployee(Long employeeId);
}
