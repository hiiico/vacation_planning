package app.web.dto;

import app.department.model.DepartmentType;
import app.employee.model.Employee;
import app.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDepartmentRequest {

    private String name;

    private Employee manager;

    private DepartmentType type;

    private List<User> employees;
}
