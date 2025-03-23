package app.department.service;

import app.department.model.Department;
import app.department.model.DepartmentType;
import app.department.repository.DepartmentRepository;
import app.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(User user) {

        Department department = departmentRepository.save(initilizeDepartment(user));
        log.info("Successfully created new department with name [%s] and type [%s]."
                .formatted(department.getName(), department.getType()));
        return department;
    }

    private Department initilizeDepartment(User user) {

        return Department.builder()
                .name("HR")
                .type(DepartmentType.MANAGEMENT)
                .manager(user)
                .build();
    }

    public Department createDepartment(String name) {
        Department department = new Department();
        department.setName(name);
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(UUID id) {
        return departmentRepository.findById(id);
    }

    public void deleteDepartment(UUID id) {
        departmentRepository.deleteById(id);
    }

    public Department createDefaultDepartment(User user) {

        Department defaultDepartment = Department.builder()
                .name("departmental_distribution")
                .type(DepartmentType.DISTRIBUTION)
                .manager(user)
                .build();

        return departmentRepository.save(defaultDepartment);
    }
}
