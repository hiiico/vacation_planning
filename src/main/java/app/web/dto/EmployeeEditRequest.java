package app.web.dto;

import app.contract.model.Contract;
import app.department.model.Department;
import app.employee.model.Employment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeEditRequest {

    private String phone;

    private Contract contract;

    private Department department;

    private Employment employmentStatus;

}
