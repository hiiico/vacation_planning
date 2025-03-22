package app.web.dto;

import app.department.model.DepartmentType;
import app.user.model.User;
import jakarta.persistence.Column;
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

    @Column(nullable = false)
    private DepartmentType name;

    @Column(nullable = false)
    private User manager;

    private List<User> employees;
}
