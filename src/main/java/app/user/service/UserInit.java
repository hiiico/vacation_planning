package app.user.service;

import app.user.model.UserRole;
import app.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInit implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public UserInit(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void run(String... args) throws Exception {

        if (!userService.getAllUser().isEmpty()) {
            return;
        }

        RegisterRequest registerUser = RegisterRequest.builder()
                .username("H.Ivanov")
                .password("Hristo1972")
                .role(UserRole.ADMIN)
                .build();
        userService.register(registerUser);
    }

}
