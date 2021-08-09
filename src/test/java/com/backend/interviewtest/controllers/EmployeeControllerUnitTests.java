package com.backend.interviewtest.controllers;

import com.backend.interviewtest.dto.EmployeeDto;
import com.backend.interviewtest.dto.EmployeeListResponseDto;
import com.backend.interviewtest.dto.EmployeeResponseDto;
import com.backend.interviewtest.dto.ResponseTemplateDto;
import com.backend.interviewtest.services.AuthorizationService;
import com.backend.interviewtest.services.EmployeeService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerUnitTests {
    @InjectMocks
    private EmployeeController employeeController;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private EmployeeService employeeService;

    private EmployeeDto employeeMock;
    private ResponseTemplateDto successResponse;
    private final String authHeaderMock = "TestAccessToken";
    private final Long employeeIdMock = Long.valueOf(1);
    private final Long invalidEmployeeIdMock = Long.valueOf(10);
    private final String employeeNotFoundMsg = "Employee Not Found";
    private final String successMsg = "Success";

    @BeforeEach
    public void setup() {
        this.employeeMock = new EmployeeDto();
        this.employeeMock.setEmployeeId(this.employeeIdMock);
        this.employeeMock.setFirstName("John");
        this.employeeMock.setLastName("Doe");
        this.employeeMock.setDepartment("IT");

        this.successResponse = new ResponseTemplateDto();
        this.successResponse.setCode(200);
        this.successResponse.setMessage(this.successMsg);
    }

    @Test
    public void testCreateEmployeeShouldReturnUnauthorizedWHenTokenIsInvalid() throws Exception {
        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(false);

        ResponseEntity<ResponseTemplateDto> response = this.employeeController
                .createEmployee(this.authHeaderMock, this.employeeMock);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        Assert.assertEquals(response.getBody(), null);
    }

    @Test
    public void testCreateEmployeeShouldCallCreateAndReturnSuccessResponseWhenAccessTokenIsValid() throws Exception {
        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(true);
        Mockito.when(this.employeeService.create(this.employeeMock))
                .thenReturn(this.employeeMock);

        ResponseEntity<ResponseTemplateDto> response = this.employeeController
                .createEmployee(this.authHeaderMock, this.employeeMock);

        Mockito.verify(this.employeeService, Mockito.times(1))
                .create(Mockito.any(EmployeeDto.class));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody().getCode(), this.successResponse.getCode());
        Assert.assertEquals(response.getBody().getMessage(), this.successResponse.getMessage());
    }

    @Test
    public void testCreateEmployeeShouldReturnFailedResponseWhenFailtoCreateEmployee() throws Exception {
        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(true);
        Mockito.when(this.employeeService.create(this.employeeMock))
                .thenReturn(null);

        ResponseEntity<ResponseTemplateDto> response = this.employeeController
                .createEmployee(this.authHeaderMock, this.employeeMock);

        Mockito.verify(this.employeeService, Mockito.times(1))
                .create(Mockito.any(EmployeeDto.class));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(response.getBody().getCode(), 500);
        Assert.assertEquals(response.getBody().getMessage(), "Create Employee Fail");
    }

    @Test
    public void testGetEmployeesShouldReturnUnauthorizedWhenTokenIsInvalid() throws Exception {
        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(false);

        ResponseEntity<EmployeeListResponseDto> response = this.employeeController.getEmployees(this.authHeaderMock);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        Assert.assertEquals(response.getBody(), null);
    }

    @Test
    public void testGetEmployeesShouldReturnAllEmployeesWhenAccessTokenIsValid() throws Exception {
        EmployeeDto secondEmployee = new EmployeeDto();
        secondEmployee.setEmployeeId(Long.valueOf(2));
        secondEmployee.setFirstName("Jimmy");
        secondEmployee.setLastName("Doe");
        secondEmployee.setDepartment("HR");

        List<EmployeeDto> employeeDtoList = new ArrayList();
        employeeDtoList.add(this.employeeMock);
        employeeDtoList.add(secondEmployee);

        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(true);
        Mockito.when(this.employeeService.getAllEmployees())
                .thenReturn(employeeDtoList);

        ResponseEntity<EmployeeListResponseDto> response = this.employeeController.getEmployees(this.authHeaderMock);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody().getCode(), 200);
        Assert.assertEquals(response.getBody().getMessage(), this.successMsg);
        Assert.assertEquals(response.getBody().getEmployees(), employeeDtoList);
    }

    @Test
    public void testGetEmployeeByEmployeeIdShouldReturnUnauthorizedWhenTokenIsInvalid() throws Exception {
        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(false);

        ResponseEntity<EmployeeListResponseDto> response = this.employeeController.getEmployees(this.authHeaderMock);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        Assert.assertEquals(response.getBody(), null);
    }

    @Test
    public void testGetEmployeeByEmployeeIdShouldReturnEmployeeWithTheCorrectEmployeeIdWhenAccessTokenIsValid() throws Exception {
        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(true);
        Mockito.when(this.employeeService.getEmployee(this.employeeIdMock))
                .thenReturn(this.employeeMock);

        ResponseEntity<EmployeeResponseDto> response = this.employeeController.getEmployeeByEmployeeId(this.authHeaderMock, this.employeeIdMock);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody().getCode(), 200);
        Assert.assertEquals(response.getBody().getMessage(), this.successMsg);
        Assert.assertEquals(response.getBody().getEmployee(), this.employeeMock);
    }

    @Test
    public void testGetEmployeeByEmployeeIdWithUnExistEmployeeIdShouldReturnEmployeeAsNullWithStatusCodeNotFoundWhenAccessTokenIsValid() throws Exception {
        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(true);
        Mockito.when(this.employeeService.getEmployee(this.invalidEmployeeIdMock))
                .thenReturn(null);

        ResponseEntity<EmployeeResponseDto> response = this.employeeController.getEmployeeByEmployeeId(this.authHeaderMock, this.invalidEmployeeIdMock);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        Assert.assertEquals(response.getBody().getCode(), 404);
        Assert.assertEquals(response.getBody().getMessage(), this.employeeNotFoundMsg);
        Assert.assertEquals(response.getBody().getEmployee(), null);
    }

    @Test
    public void testUpdateEmployeeShouldReturnUnauthorizedWhenTokenIsInvalid() throws Exception {
        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(false);

        ResponseEntity<EmployeeResponseDto> response = this.employeeController.updateEmployee(
                this.authHeaderMock,
                this.employeeIdMock,
                this.employeeMock
        );

        Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        Assert.assertEquals(response.getBody(), null);
    }

    @Test
    public void testUpdateEmployeeWithExistingEmployeeIdShouldReturnUpdatedEmployeeWhenAccessTokenIsValid() throws Exception {
        EmployeeDto updatedEmployeeMock = new EmployeeDto();
        updatedEmployeeMock.setFirstName(this.employeeMock.getFirstName());
        updatedEmployeeMock.setLastName(this.employeeMock.getLastName());
        updatedEmployeeMock.setDepartment("HR");

        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(true);
        Mockito.when(this.employeeService.updateEmployee(this.employeeIdMock, updatedEmployeeMock))
                .thenReturn(updatedEmployeeMock);

        ResponseEntity<EmployeeResponseDto> response = this.employeeController.updateEmployee(
                this.authHeaderMock,
                this.employeeIdMock,
                updatedEmployeeMock
        );

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody().getCode(), 200);
        Assert.assertEquals(response.getBody().getMessage(), this.successMsg);
        Assert.assertEquals(response.getBody().getEmployee(), updatedEmployeeMock);
    }

    @Test
    public void testUpdateEmployeeWithUnExistingEmployeeIdShouldReturnNotFoundWhenAccessTokenIsValid() throws Exception {
        EmployeeDto updatedEmployeeMock = new EmployeeDto();
        updatedEmployeeMock.setFirstName(this.employeeMock.getFirstName());
        updatedEmployeeMock.setLastName(this.employeeMock.getLastName());
        updatedEmployeeMock.setDepartment("HR");

        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(true);
        Mockito.when(this.employeeService.updateEmployee(this.invalidEmployeeIdMock, updatedEmployeeMock))
                .thenReturn(null);

        ResponseEntity<EmployeeResponseDto> response = this.employeeController.updateEmployee(
                this.authHeaderMock,
                this.invalidEmployeeIdMock,
                updatedEmployeeMock
        );

        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        Assert.assertEquals(response.getBody().getCode(), 404);
        Assert.assertEquals(response.getBody().getMessage(), this.employeeNotFoundMsg);
        Assert.assertEquals(response.getBody().getEmployee(), null);
    }

    @Test
    public void testDeleteEmployeeShouldReturnUnauthorizedWhenTokenIsInvalid() throws Exception {
        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(false);

        ResponseEntity<ResponseTemplateDto> response = this.employeeController.deleteEmployee(
                this.authHeaderMock,
                this.employeeIdMock
        );

        Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        Assert.assertEquals(response.getBody(), null);
    }

    @Test
    public void testDeleteEmployeeWithExistingEmployeeIdShouldReturnSuccessResponseWhenAccessTokenIsValid() throws Exception {
        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(true);
        Mockito.when(this.employeeService.deleteEmployee(this.employeeIdMock))
                .thenReturn(this.employeeMock);

        ResponseEntity<ResponseTemplateDto> response = this.employeeController
                .deleteEmployee(this.authHeaderMock, this.employeeIdMock);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody().getCode(), this.successResponse.getCode());
        Assert.assertEquals(response.getBody().getMessage(), this.successResponse.getMessage());
    }

    @Test
    public void testDeleteEmployeeWithUnExistingEmployeeIdShouldReturnNotFoundResponseWhenAccessTokenIsValid() throws Exception {
        Mockito.when(this.authorizationService.isAuthorized(this.authHeaderMock))
                .thenReturn(true);
        Mockito.when(this.employeeService.deleteEmployee(this.invalidEmployeeIdMock))
                .thenReturn(null);

        ResponseEntity<ResponseTemplateDto> response = this.employeeController
                .deleteEmployee(this.authHeaderMock, this.invalidEmployeeIdMock);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        Assert.assertEquals(response.getBody().getCode(), 404);
        Assert.assertEquals(response.getBody().getMessage(), this.employeeNotFoundMsg);
    }
}