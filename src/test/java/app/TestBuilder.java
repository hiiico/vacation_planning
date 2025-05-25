package app;

import app.user.model.Country;
import app.user.model.User;
import app.user.model.UserRole;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class TestBuilder {

    public static User aRandomUser() {

        return User.builder()
                .id(UUID.randomUUID())
                .username("administrator")
                .password("Hristo1972")
                .role(UserRole.ADMIN)
                .country(Country.BULGARIA)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }
}
