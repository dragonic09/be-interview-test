package com.backend.interviewtest.services;

import com.backend.interviewtest.dto.EmployeeDto;
import com.backend.interviewtest.dto.ResponseTemplateDto;
import com.backend.interviewtest.entities.Employee;
import com.backend.interviewtest.repositories.AccessTokenRepository;
import com.backend.interviewtest.repositories.EmployeeRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceUnitTests {
    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    private EmployeeDto employeeDtoMock;
    private Employee employeeEntityMock;
    private Employee savedEmployeeEntityMock;
    @BeforeEach
    public void setup() throws Exception {
        this.employeeService = new EmployeeServiceImpl(this.employeeRepository);
        this.employeeDtoMock = new EmployeeDto();
        this.employeeDtoMock.setFirstName("John");
        this.employeeDtoMock.setLastName("Doe");
        this.employeeDtoMock.setDepartment("IT");

        this.employeeEntityMock = new Employee();
        this.employeeEntityMock.setFirstName("John");
        this.employeeEntityMock.setLastName("Doe");
        this.employeeEntityMock.setDepartment("IT");

        this.savedEmployeeEntityMock = new Employee();
        this.savedEmployeeEntityMock.setId(Long.valueOf(1));
        this.savedEmployeeEntityMock.setFirstName("John");
        this.savedEmployeeEntityMock.setLastName("Doe");
        this.savedEmployeeEntityMock.setDepartment("IT");
    }

    @Test
    public void testCreateEmployeeSuccessfullyShouldReturnCreatedEmployee() throws Exception {
        Mockito.when(this.employeeRepository.save(Mockito.any(Employee.class)))
                .thenReturn(this.savedEmployeeEntityMock);

        EmployeeDto newEmployee = this.employeeService
                .create(this.employeeDtoMock);

        Mockito.verify(this.employeeRepository, Mockito.times(1))
                .save(Mockito.any(Employee.class));
        Assert.assertEquals(newEmployee.getEmployeeId(), this.savedEmployeeEntityMock.getId());
        Assert.assertEquals(newEmployee.getFirstName(), this.employeeDtoMock.getFirstName());
        Assert.assertEquals(newEmployee.getLastName(), this.employeeDtoMock.getLastName());
        Assert.assertEquals(newEmployee.getDepartment(), this.employeeDtoMock.getDepartment());
    }

    @Test
    public void testCreateEmployeeFailShouldReturnNull() throws Exception {
        Mockito.when(this.employeeRepository.save(Mockito.any(Employee.class)))
                .thenReturn(null);

        EmployeeDto newEmployee = this.employeeService
                .create(this.employeeDtoMock);

        Mockito.verify(this.employeeRepository, Mockito.times(1))
                .save(Mockito.any(Employee.class));
        Assert.assertEquals(newEmployee, null);
    }

    @Test
    public void testUpdateEmployeeFailShouldReturnNull() throws Exception {
        Mockito.when(this.employeeRepository.findById(Long.valueOf(2)))
                .thenReturn(Optional.empty());

        EmployeeDto newEmployee = this.employeeService
                .updateEmployee(Long.valueOf(2), this.employeeDtoMock);

        Mockito.verify(this.employeeRepository, Mockito.times(0))
                .save(Mockito.any(Employee.class));
        Assert.assertEquals(newEmployee, null);
    }

    @Test
    public void testUpdateEmployeeWithExistingEmployeeIdShouldReturnUpdatedEmployee() throws Exception {
        this.employeeDtoMock.setFirstName("Jim");
        this.employeeDtoMock.setLastName("Doe");
        this.employeeDtoMock.setDepartment("HR");

        Employee updatedEmployeeMock = new Employee();
        updatedEmployeeMock.setId(this.savedEmployeeEntityMock.getId());
        updatedEmployeeMock.setFirstName(this.employeeDtoMock.getFirstName());
        updatedEmployeeMock.setLastName(this.employeeDtoMock.getLastName());
        updatedEmployeeMock.setDepartment(this.employeeDtoMock.getDepartment());

        Mockito.when(this.employeeRepository.findById(this.savedEmployeeEntityMock.getId()))
                .thenReturn(Optional.of(this.savedEmployeeEntityMock));
        Mockito.when(this.employeeRepository.save(Mockito.any(Employee.class)))
                .thenReturn(updatedEmployeeMock);

        EmployeeDto updatedEmployeeDto = this.employeeService
                .updateEmployee(this.savedEmployeeEntityMock.getId(), this.employeeDtoMock);

        Mockito.verify(this.employeeRepository, Mockito.times(1))
                .save(Mockito.any(Employee.class));
        Assert.assertEquals(updatedEmployeeDto.getEmployeeId(), updatedEmployeeMock.getId());
        Assert.assertEquals(updatedEmployeeDto.getFirstName(), updatedEmployeeMock.getFirstName());
        Assert.assertEquals(updatedEmployeeDto.getLastName(), updatedEmployeeMock.getLastName());
        Assert.assertEquals(updatedEmployeeDto.getDepartment(), updatedEmployeeMock.getDepartment());
    }

    @Test
    public void testGetAllEmployeesShouldReturnListOfEmployees() throws Exception {
        List<Employee> employeesMock = new ArrayList();

        Employee secondEmployeeMock = new Employee();
        secondEmployeeMock.setId(Long.valueOf(2));
        secondEmployeeMock.setFirstName("Jim");
        secondEmployeeMock.setLastName("Doe");
        secondEmployeeMock.setDepartment("HR");

        employeesMock.add(this.savedEmployeeEntityMock);
        employeesMock.add(secondEmployeeMock);

        Mockito.when(this.employeeRepository.findAll())
                .thenReturn(employeesMock);

        List<EmployeeDto> employees = this.employeeService
                .getAllEmployees();

        Assert.assertEquals(employees.size(), employeesMock.size());
        Assert.assertEquals(employees.get(0).getEmployeeId(), employeesMock.get(0).getId());
        Assert.assertEquals(employees.get(1).getEmployeeId(), employeesMock.get(1).getId());
    }

    @Test
    public void testGetEmployeeShouldReturnNullWhenEmployeeIdIsnotExist() throws Exception {
        Mockito.when(this.employeeRepository.findById(Long.valueOf(2)))
                .thenReturn(Optional.empty());

        EmployeeDto employee = this.employeeService
                .getEmployee(Long.valueOf(2));

        Assert.assertEquals(employee, null);
    }

    @Test
    public void testGetEmployeeShouldReturnEmployeesWhenEmployeeIdExist() throws Exception {
        Mockito.when(this.employeeRepository.findById(this.savedEmployeeEntityMock.getId()))
                .thenReturn(Optional.of(this.savedEmployeeEntityMock));

        EmployeeDto employee = this.employeeService
                .getEmployee(this.savedEmployeeEntityMock.getId());

        Assert.assertEquals(employee.getEmployeeId(), this.savedEmployeeEntityMock.getId());
        Assert.assertEquals(employee.getFirstName(), this.savedEmployeeEntityMock.getFirstName());
        Assert.assertEquals(employee.getLastName(), this.savedEmployeeEntityMock.getLastName());
        Assert.assertEquals(employee.getDepartment(), this.savedEmployeeEntityMock.getDepartment());
    }

    @Test
    public void testDeleteEmployeeShouldReturnNullWhenEmployeeIdIsNotExist() throws Exception {
        Mockito.when(this.employeeRepository.findById(Long.valueOf(2)))
                .thenReturn(Optional.empty());

        EmployeeDto newEmployee = this.employeeService
                .deleteEmployee(Long.valueOf(2));

        Mockito.verify(this.employeeRepository, Mockito.times(0))
                .delete(Mockito.any(Employee.class));
        Assert.assertEquals(newEmployee, null);
    }

    @Test
    public void testDeleteEmployeeShouldReturnEmployeeThatGotDeleted() throws Exception {
        Mockito.when(this.employeeRepository.findById(this.savedEmployeeEntityMock.getId()))
                .thenReturn(Optional.of(this.savedEmployeeEntityMock));

        EmployeeDto deletedEmployee = this.employeeService
                .deleteEmployee(this.savedEmployeeEntityMock.getId());

        Mockito.verify(this.employeeRepository, Mockito.times(1))
                .delete(Mockito.any(Employee.class));
        Assert.assertEquals(deletedEmployee.getEmployeeId(), this.savedEmployeeEntityMock.getId());
        Assert.assertEquals(deletedEmployee.getFirstName(), this.savedEmployeeEntityMock.getFirstName());
        Assert.assertEquals(deletedEmployee.getLastName(), this.savedEmployeeEntityMock.getLastName());
        Assert.assertEquals(deletedEmployee.getDepartment(), this.savedEmployeeEntityMock.getDepartment());
    }

}
