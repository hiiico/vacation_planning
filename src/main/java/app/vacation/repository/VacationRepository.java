package app.vacation.repository;

import app.vacation.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
    List<Vacation> findByUserId(UUID userId);
}