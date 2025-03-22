package app.web.mapper;

import app.user.model.User;
import app.web.dto.UserRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static UserRequest mapUserToUserEditRequest(User user) {
        return UserRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .build();
    }
}
