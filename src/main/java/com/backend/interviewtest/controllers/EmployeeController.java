package com.backend.interviewtest.controllers;

import com.backend.interviewtest.dto.EmployeeDto;
import com.backend.interviewtest.dto.EmployeeListResponseDto;
import com.backend.interviewtest.dto.EmployeeResponseDto;
import com.backend.interviewtest.dto.ResponseTemplateDto;
import com.backend.interviewtest.services.AuthorizationService;
import com.backend.interviewtest.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class EmployeeController {
    private final AuthorizationService authorizationService;
    private final EmployeeService employeeService;

    @Autowired
    EmployeeController(AuthorizationService authorizationService, EmployeeService employeeService) {
        this.authorizationService = authorizationService;
        this.employeeService = employeeService;
    }

    @PostMapping("/employees")
    public ResponseEntity<ResponseTemplateDto> createEmployee(
            @RequestHeader(value="Authorization") String authHeader,
            @RequestBody EmployeeDto employee
    ) {
        Boolean isAuthorized = this.authorizationService.isAuthorized(authHeader);
        if(!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        EmployeeDto newEmployee = this.employeeService.create(employee);
        ResponseTemplateDto responseTemplateDto = new ResponseTemplateDto();
        responseTemplateDto.setCode(200);
        responseTemplateDto.setMessage("Success");

        if (Objects.isNull(newEmployee)) {
            responseTemplateDto.setCode(500);
            responseTemplateDto.setMessage("Create Employee Fail");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseTemplateDto);
        }
        return ResponseEntity.ok()
                .body(responseTemplateDto);
    }

    @GetMapping("/employees")
    public ResponseEntity<EmployeeListResponseDto> getEmployees(@RequestHeader(value="Authorization") String authHeader) {
        Boolean isAuthorized = this.authorizationService.isAuthorized(authHeader);
        if(!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        List<EmployeeDto> employees = this.employeeService.getAllEmployees();

        EmployeeListResponseDto employeeListResponseDto = new EmployeeListResponseDto();
        employeeListResponseDto.setCode(200);
        employeeListResponseDto.setMessage("Success");
        employeeListResponseDto.setEmployees(employees);

        return ResponseEntity.ok()
                .body(employeeListResponseDto);
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeByEmployeeId(
            @RequestHeader(value="Authorization") String authHeader,
            @PathVariable Long employeeId
    ) {
        Boolean isAuthorized = this.authorizationService.isAuthorized(authHeader);
        if(!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        EmployeeDto employee = this.employeeService.getEmployee(employeeId);

        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto();
        employeeResponseDto.setEmployee(employee);
        employeeResponseDto.setCode(200);
        employeeResponseDto.setMessage("Success");

        if (Objects.isNull(employee)) {
            employeeResponseDto.setCode(404);
            employeeResponseDto.setMessage("Employee Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(employeeResponseDto);
        }

        return ResponseEntity.ok()
                .body(employeeResponseDto);
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @RequestHeader(value="Authorization") String authHeader,
            @PathVariable Long employeeId,
            @RequestBody EmployeeDto employee
    ) {
        Boolean isAuthorized = this.authorizationService.isAuthorized(authHeader);
        if(!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        EmployeeDto updatedEmployee = this.employeeService.updateEmployee(employeeId, employee);

        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto();
        employeeResponseDto.setEmployee(updatedEmployee);
        employeeResponseDto.setCode(200);
        employeeResponseDto.setMessage("Success");

        if (Objects.isNull(updatedEmployee)) {
            employeeResponseDto.setCode(404);
            employeeResponseDto.setMessage("Employee Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(employeeResponseDto);
        }

        return ResponseEntity.ok()
                .body(employeeResponseDto);
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<ResponseTemplateDto> deleteEmployee(
            @RequestHeader(value="Authorization") String authHeader,
            @PathVariable Long employeeId
    ) {
        Boolean isAuthorized = this.authorizationService.isAuthorized(authHeader);
        if(!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        EmployeeDto updatedEmployee = this.employeeService.deleteEmployee(employeeId);

        ResponseTemplateDto responseTemplateDto = new ResponseTemplateDto();
        responseTemplateDto.setCode(200);
        responseTemplateDto.setMessage("Success");

        if (Objects.isNull(updatedEmployee)) {
            responseTemplateDto.setCode(404);
            responseTemplateDto.setMessage("Employee Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(responseTemplateDto);
        }

        return ResponseEntity.ok()
                .body(responseTemplateDto);
    }
}
