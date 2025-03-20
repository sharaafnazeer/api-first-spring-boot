package com.sharaafnazeer.apifirstspringboot.controllers;

import com.sharaafnazeer.apifirstspringboot.api.EmployeesApi;
import com.sharaafnazeer.apifirstspringboot.model.Employee;
import com.sharaafnazeer.apifirstspringboot.model.ModelApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController implements EmployeesApi {

    List<Employee> employees = new ArrayList<>();

    @Override
    public ResponseEntity<List<Employee>> findEmployees() {
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(Long employeeId) {
        return ResponseEntity.of(employees.stream()
                .filter(employee -> employee.getId().equals(employeeId)).findFirst());
    }

    @Override
    public ResponseEntity<ModelApiResponse> addEmployee(Employee employee) {
        employees.add(employee);

        ModelApiResponse apiResponse = new ModelApiResponse();
        apiResponse.code(200);
        apiResponse.setMessage("Employee added successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<ModelApiResponse> deleteEmployee(Long employeeId) {

        if (employeeId == null) {
            ResponseEntity.badRequest().build();
        }

        Optional<Employee> existingEmployee = employees.stream().filter(employee ->
                employee.getId().equals(employeeId)).findFirst();

        if (existingEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        employees = employees.stream().filter(employee ->
                !employee.getId().equals(employeeId)).toList();

        ModelApiResponse apiResponse = new ModelApiResponse();
        apiResponse.code(200);
        apiResponse.setMessage("Employee deleted successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<ModelApiResponse> updateEmployee(Long employeeId, Employee newEmployee) {

        if (employeeId == null) {
            ResponseEntity.badRequest().build();
        }

        Optional<Employee> existingOptionalEmployee = employees.stream().filter(employee ->
                employee.getId().equals(employeeId)).findFirst();

        if (existingOptionalEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Employee> newEmployees = new ArrayList<>(employees.stream().filter(employee ->
                !employee.getId().equals(employeeId)).toList());
        newEmployee.setId(existingOptionalEmployee.get().getId());
        newEmployees.add(newEmployee);
        employees = newEmployees;

        ModelApiResponse apiResponse = new ModelApiResponse();
        apiResponse.code(200);
        apiResponse.setMessage("Employee updated successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<List<Employee>> findEmployeesByStatus(String status) {

        List<String> statuses = Arrays.stream(status.split(",")).toList();

        List<Employee> filteredEmployees = employees.stream()
                .filter(employee -> statuses.contains(employee.getStatus().getValue())).toList();

        return ResponseEntity.ok(filteredEmployees);
    }
}
