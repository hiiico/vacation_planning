package app.user;

import app.employee.service.EmployeeService;
import app.exception.DomainException;
import app.exception.UsernameAlreadyExist;
import app.notification.service.NotificationService;
import app.security.AuthenticationDetails;
import app.user.model.Country;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private UserService userService;

    @ParameterizedTest
    @MethodSource("userRolesArguments")
    void whenChangeUserRole_thenCorrectRoleIsAssigned(UserRole currentUserRole, UserRole expectedUserRole) {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .role(currentUserRole)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.switchRole(userId);
        
        assertEquals(expectedUserRole, user.getRole());
    }

    private static Stream<Arguments> userRolesArguments() {

        return Stream.of(
                Arguments.of(UserRole.USER, UserRole.MANAGER),
                Arguments.of(UserRole.MANAGER, UserRole.USER),
                Arguments.of(UserRole.ADMIN, UserRole.USER)
        );
    }

    // test design pattern Given -> When & Then
    // instead of test design pattern AAA -> Arrange, Act,Assert
    @Test
    void givenMissingUserFromDatabase_whenEditUserDetails_thenExceptionIsThrow() {

        UUID userId = UUID.randomUUID();
        UserEditRequest dto = UserEditRequest.builder().build();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class,() -> userService.editUserDetails(userId, dto));
    }

    @Test
    void givenUserWithRoleAdmin_whenSwitchRole_thenUserReceivesUserRole() {

        //Given
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .role(UserRole.ADMIN)
                .build();
        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.switchRole(userId);

        //Then
        assertThat(user.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void givenExistingUser_whenEditTheirProfileWithActualEmail_thenChangeTheirDetailsSaveNotificationPreferenceAndSaveToDatabase() {

        // Given
        UUID userId = UUID.randomUUID();
        UserEditRequest dto = UserEditRequest.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john@mail.com")
                .profilePicture("www.image.com")
                .build();

        User user = User.builder().build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userService.editUserDetails(userId, dto);

        // Then
        assertEquals("John", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("john@mail.com", user.getEmail());
        assertEquals("www.image.com", user.getProfilePicture());

        verify(notificationService, times(1)).saveNotificationPreference(userId, true, dto.getEmail());
        //verify(notificationService, never()).saveNotificationPreference(userId, true, dto.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenExistingUser_whenEditTheirProfileWithEmptyEmail_thenChangeTheirDetailsSaveNotificationPreferenceAndSaveToDatabase() {

        // Given
        UUID userId = UUID.randomUUID();
        UserEditRequest dto = UserEditRequest.builder()
                .firstName("John")
                .lastName("Smith")
                .email("")
                .profilePicture("www.image.com")
                .build();

        User user = User.builder().build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userService.editUserDetails(userId, dto);

        // Then
        assertEquals("John", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("", user.getEmail());
        assertEquals("www.image.com", user.getProfilePicture());

        verify(notificationService, times(1)).saveNotificationPreference(userId, false, null);
      //  verify(notificationService, never()).saveNotificationPreference(userId, false, dto.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenMissingUserFromDatabase_whenLoadUsername_thenExceptionIsThrow() {

        String username ="Pesho";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    void givenExistingUserFromDatabase_whenLoadUsername_thenReturnCorrectAuthenticationMetadata() {

        String username ="Pesho";
        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(true)
                .password("123123")
                .role(UserRole.USER)
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails authenticationMetadata = userService.loadUserByUsername(username);

        assertInstanceOf(AuthenticationDetails.class, authenticationMetadata);
        assertEquals(username, authenticationMetadata.getUsername());
        AuthenticationDetails result = (AuthenticationDetails) authenticationMetadata;
        assertEquals(user.getId(), result.getUserId());
        assertEquals(username, result.getUsername());
        assertEquals(user.isActive(), result.isActive());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getRole(), result.getUserRole());
        assertThat(result.getAuthorities()).hasSize(1);
        assertEquals("ROLE_USER", result.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void givenExistingUsername_whenRegister_thenExceptionIsThrow() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("administrator")
                .password("123123")
                .country(Country.BULGARIA)
                .build();
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExist.class, () -> userService.register(registerRequest));

        verify(userRepository, never()).save(any());
        verify(notificationService, never()).saveNotificationPreference(any(), anyBoolean(), anyString());
    }

// when in UserService Kafka is open Test not pass
//    @Test
//    void givenNotExistingUser_whenRegister_thenHappyPath() {
//
//        RegisterRequest registerRequest = RegisterRequest.builder()
//                .username("administrator")
//                .password("123123")
//                .country(Country.BULGARIA)
//                .build();
//        User user = User.builder()
//                .id(UUID.randomUUID())
//                .build();
//        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
//        when(userRepository.save(any())).thenReturn(user);
//
//        userService.register(registerRequest);
//
//        verify(notificationService, times(1)).saveNotificationPreference(user.getId(),false,null);
//    }

    @Test
    void givenUserStatusActive_whenSwitchStatus_thenStatusInactive() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(true)
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.switchStatus(user.getId());

        assertFalse(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserStatusInactive_whenSwitchStatus_thenStatusActive() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(false)
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.switchStatus(user.getId());

        assertTrue(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUsersInDatabase_whenGetAllUsers_thenAllUsers() {

        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userRepository.findAll();

        assertThat(allUsers).hasSize(2);
    }
}
