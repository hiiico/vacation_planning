package app.web.dto;

import app.employee.model.Employee;
import app.vacation.model.AbsencesReason;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacationEditRequest {

    @NotNull
    private Employee employee;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private boolean approved;

    @NotNull
    private AbsencesReason absencesReason;

}
