package app.department.service;

import app.department.model.Department;
import app.department.model.DepartmentType;
import app.department.repository.DepartmentRepository;
import app.employee.service.EmployeeService;
import app.web.dto.RegisterDepartmentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, EmployeeService employeeService) {
        this.departmentRepository = departmentRepository;

        this.employeeService = employeeService;
    }

    public List<String> getAllDepartmentsTypes() {

        List<Department> departments = departmentRepository.findAll();
        List<String> types = new ArrayList<>(Arrays.stream(DepartmentType.values()).map(DepartmentType::toString).toList());
        List<String> newTypes = new ArrayList<>();

        for (String type : types) {

            if (!departments.isEmpty()) {
                for(Department department: departments) {
                    if (!String.valueOf(department.getType()).equals(type)) {
                        newTypes.add(type);
                    }
                }
            } else {
                newTypes.add(type);
            }

        }

        return newTypes;
    }

    public void register(RegisterDepartmentRequest registerDepartmentRequest) {

        Department department = initializeDepartment(registerDepartmentRequest);
        departmentRepository.save(department);
        employeeService.setDepartment(department, department.getManager().getEmployeeId());

        log.info("Successfully created new department with name [%s]."
                .formatted(registerDepartmentRequest.getName()));
    }

    private Department initializeDepartment(RegisterDepartmentRequest registerDepartmentRequest) {
        return Department.builder()
                .name(registerDepartmentRequest.getName())
                .manager(registerDepartmentRequest.getManager())
                .type(registerDepartmentRequest.getType())
                .build();
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}
