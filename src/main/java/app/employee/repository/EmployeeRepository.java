package app.employee.repository;

import app.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByUsername(String username);
}
