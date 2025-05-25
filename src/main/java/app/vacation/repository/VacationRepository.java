package app.vacation.repository;

import app.vacation.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VacationRepository extends JpaRepository<Vacation, Long> {

    Optional<Vacation> findByEmployee_EmployeeId(UUID employeeId);

}