package app.web.dto;

import app.contract.model.ContractType;
import app.employee.model.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
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
    private String type;

    @NotNull
    private Employee employee;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private String description;

    @NotNull
    private int leaveDays;

    @NotNull
    boolean renewalAllowed;

    @NotNull
    boolean active;


}
