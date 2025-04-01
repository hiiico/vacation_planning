package app.web.dto;

import app.contract.model.ContractType;
import app.department.model.Department;
import app.employee.model.Employee;
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
public class RegisterContractRequest {

    @NotNull
    private Employee employee;

    @NotNull
    private Employee manager;

    @NotNull
    private ContractType type;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private String description;


}
