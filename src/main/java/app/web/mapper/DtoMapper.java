package app.web.mapper;

import app.employee.model.Employee;
import app.user.model.User;
import app.vacation.model.Vacation;
import app.web.dto.EmployeeEditRequest;
import app.web.dto.UserEditRequest;
import app.web.dto.VacationEditRequest;
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

}
