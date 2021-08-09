package com.backend.interviewtest.services;

import com.backend.interviewtest.dto.EmployeeDto;
import com.backend.interviewtest.entities.Employee;
import com.backend.interviewtest.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeDto create(EmployeeDto employeeData) {
        Employee newEmployee = new Employee();
        newEmployee = this.mapEmployeeDtoToEntity(employeeData, newEmployee);

        newEmployee = this.employeeRepository.save(newEmployee);

        if(Objects.isNull(newEmployee)) {
            return null;
        }

        EmployeeDto newEmployeeDto = this.mapEmployeeToDto(newEmployee);
        return newEmployeeDto;
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        Optional<Employee> employeeOptional = this.employeeRepository.findById(employeeId);
        if (employeeOptional.isEmpty()) {
            return null;
        }

        Employee employee = employeeOptional.get();
        employee = this.mapEmployeeDtoToEntity(employeeDto, employee);
        employee = this.employeeRepository.save(employee);

        EmployeeDto updatedEmployeeDto = this.mapEmployeeToDto(employee);
        return updatedEmployeeDto;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = this.employeeRepository.findAll();
        return employees.stream()
                .map(employee -> this.mapEmployeeToDto(employee))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto getEmployee(Long employeeId) {
        Optional<Employee> employeeOptional = this.employeeRepository.findById(employeeId);
        if (employeeOptional.isEmpty()) {
            return null;
        }

        Employee employee = employeeOptional.get();
        return this.mapEmployeeToDto(employee);
    }

    @Override
    public EmployeeDto deleteEmployee(Long employeeId) {
        Optional<Employee> employeeOptional = this.employeeRepository.findById(employeeId);
        if (employeeOptional.isEmpty()) {
            return null;
        }

        Employee employee = employeeOptional.get();
        this.employeeRepository.delete(employee);

        EmployeeDto deletedEmployeeDto = this.mapEmployeeToDto(employee);
        return deletedEmployeeDto;
    }

    private EmployeeDto mapEmployeeToDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(employee.getId());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setDepartment(employee.getDepartment());
        return employeeDto;
    }

    private Employee mapEmployeeDtoToEntity(EmployeeDto employeeDto, Employee employee) {
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setDepartment(employeeDto.getDepartment());
        return employee;
    }
}
