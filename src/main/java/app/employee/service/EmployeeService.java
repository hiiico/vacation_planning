package app.employee.service;

import app.department.model.Department;
import app.employee.model.Employee;
import app.employee.model.EmployeeRole;
import app.employee.model.Employment;
import app.employee.repository.EmployeeRepository;
import app.exception.DomainException;
import app.user.model.User;
import app.user.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    public void createEmployee(User user) {

        Optional<Employee> employeeOptional = employeeRepository.findByUsername(user.getUsername());

        if(employeeOptional.isEmpty()) {

            Employee employee = employeeRepository.save(initializeEmployee(user));

            log.info("Successfully created new employee with first name [%s] and last name [%s]"
                    .formatted(employee.getFirstName(), employee.getLastName()));

            saveEmployeeRole(user);
        }
    }

    private Employee initializeEmployee(User user) {

        return Employee.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(EmployeeRole.EMPLOYEE)
                .email(user.getEmail())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    public Employee getById(UUID id) {
    return employeeRepository.findById(id).orElseThrow(
            () -> new DomainException("Employee with id [%s] does not exist."));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }


    public void switchEmployment(UUID id) {

        Employee employee = getById(id);
        if(employee.getEmployment() == Employment.ENGAGED) {
            employee.setEmployment(Employment.VACATION);
        } else {
            employee.setEmployment(Employment.ENGAGED);
        }
        employeeRepository.save(employee);
    }

    public void saveEmployeeRole(User user) {

        Optional<Employee> employeeOptional = employeeRepository.findByUsername(user.getUsername());

        if (employeeOptional.isPresent()) {

        UserRole role = user.getRole();
        Employee employee = employeeOptional.get();

        switch (role) {
            case USER -> employee.setRole(EmployeeRole.EMPLOYEE);
            case MANAGER -> employee.setRole(EmployeeRole.MANAGER);
            case ADMIN -> employee.setRole(EmployeeRole.ADMIN);
        }
        employeeRepository.save(employee);
        }
    }

    public List<Employee> getAllManagers() {

        return employeeRepository.findAll()
                .stream()
                .filter(
                        employee -> employee.getRole() == EmployeeRole.MANAGER
                                && employee.getDepartment() == null)
                .toList();
    }

    public void setDepartment(Department department, UUID employeeId) {

        Employee employee = getById(employeeId);
        employee.setDepartment(department);

        employeeRepository.save(employee);
    }

    public Employee getByUsername(String username) {

        Optional<Employee> employeeOptional = employeeRepository.findByUsername(username);

        return employeeOptional.get();
    }

}
