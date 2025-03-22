package app.user.service;

import app.user.model.UserRole;
import app.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInit implements CommandLineRunner {

    private final UserService employeeService;

    @Autowired
    public UserInit(UserService employeeService) {
        this.employeeService = employeeService;
    }


    @Override
    public void run(String... args) throws Exception {

        if (!employeeService.getAllUser().isEmpty()) {
            return;
        }

        RegisterRequest registerUser = RegisterRequest.builder()
                .username("H.Ivanov")
                .password("Hristo1972")
                .role(UserRole.ADMIN)
                .build();
        employeeService.register(registerUser);
    }

}
