package app.web.mapper;

import app.user.model.User;
import app.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DtoMapperUnitTest {

    @Test
    void givenHappyPath_whenMappingUserToUserEditRequest() {

        User user = User.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john@mail.com")
                .profilePicture("image.com")
                .build();

        UserEditRequest result = DtoMapper.mapUserToUserEditRequest(user);

        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getProfilePicture(), result.getProfilePicture());
    }
}
