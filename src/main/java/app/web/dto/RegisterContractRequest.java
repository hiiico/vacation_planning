package app.web.dto;

import app.contract.model.ContractType;
import app.user.model.User;
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
    private User employee;

    @NotNull
    private User manager;

    @NotNull
    private ContractType type;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

}
