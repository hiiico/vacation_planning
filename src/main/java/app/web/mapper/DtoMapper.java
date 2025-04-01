package app.web.mapper;

import app.employee.model.Employee;
import app.user.model.User;
import app.web.dto.EmployeeEditRequest;
import app.web.dto.UserEditRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static UserEditRequest mapUserToUserEditRequest(User user) {
        return UserEditRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    public static EmployeeEditRequest mapEmployeeToEmployeeEditRequest(Employee employee) {

    return EmployeeEditRequest.builder()
            .phone(employee.getPhone())
            .contract(employee.getContract())
            .department(employee.getDepartment())
            .employmentStatus(employee.getEmployment())
            .build();
    }
}
