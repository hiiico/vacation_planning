package app.user.service;

import app.user.model.Country;
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

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("administrator")
                .password("admin1234")
                .country(Country.GERMANY)
                .build();
        userService.registerDefaultUser(registerRequest);

    }

}
