package app.user.service;

import app.employee.service.EmployeeService;
import app.event.EventMessage;
import app.exception.DomainException;
import app.exception.UsernameAlreadyExist;
import app.notification.service.NotificationService;
import app.security.AuthenticationDetails;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final EmployeeService employeeService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, NotificationService notificationService, EmployeeService employeeService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
        this.employeeService = employeeService;
    }

    public void registerDefaultUser(RegisterRequest registerRequest) {

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .country(registerRequest.getCountry())
                .role(UserRole.ADMIN)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        userRepository.save(user);

        log.info("Successfully created new user account for username [%s] and id [%s]"
                .formatted(registerRequest.getUsername(), user.getId()));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public User register(RegisterRequest registerRequest) {

        Optional<User> userOptional = userRepository.findByUsername(registerRequest.getUsername());

        if(userOptional.isPresent()) {
            throw new UsernameAlreadyExist("Username %s already exist.".formatted(registerRequest.getUsername()));
        }

        User user = userRepository.save(initializeUser(registerRequest));
        notificationService.saveNotificationPreference(user.getId(), false, null);

        log.info("Successfully created new user account for username [%s] and id [%s]"
                .formatted(registerRequest.getUsername(), user.getId()));
        return user;
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void editUserDetails(UUID userId, UserEditRequest userEditRequest) {

        User user = getById(userId);
        StringBuilder message = new StringBuilder((String.format("Hello %s, ", user.getUsername())));
        boolean changes = false;

        if (!(user.getFirstName()).equals(userEditRequest.getFirstName())) {
            user.setFirstName(userEditRequest.getFirstName());
            message.append(String.format("your first name has been set to [%s], ", user.getFirstName()));
            changes = true;
        }

        if (!(user.getLastName()).equals(userEditRequest.getLastName())) {
            user.setLastName(userEditRequest.getLastName());
            message.append(String.format("your last name has been set to [%s], ", user.getLastName()));
            changes = true;
        }

        if (!(user.getProfilePicture()).equals(userEditRequest.getProfilePicture())) {
            user.setProfilePicture(userEditRequest.getProfilePicture());
            message.append(String.format("your profile picture has been set to [%s], ", user.getProfilePicture()));
            changes = true;
        }

        if (!(user.getEmail()).equals(userEditRequest.getEmail())) {
            if (user.getEmail().isBlank() && !userEditRequest.getEmail().isBlank()) {
                user.setEmail(userEditRequest.getEmail());
                message.append(String.format("you are successfully registered in Vacation planner and your email has been set to [%s]!", user.getEmail()));
                changes = true;
            } else if (userEditRequest.getEmail().isBlank()) {
                message.append(String.format("your contact email [%s] has been successfully removed, ", user.getEmail()));
                notificationService.sendNotification(userId, "Changes", String.valueOf(message));
                user.setEmail(userEditRequest.getEmail());
                notificationService.saveNotificationPreference(userId, false, null);
            } else {
                message.append(String.format("your contact email has been set to [%s],", userEditRequest.getEmail()));
                user.setEmail(userEditRequest.getEmail());
                changes = true;
            }
        }

        notificationService.saveNotificationPreference(userId, true, user.getEmail());
        userRepository.save(user);

        if (!user.getEmail().isBlank() && changes) {
            notificationService.sendNotification(userId, "Changes", String.valueOf(message));
        }

        // TODO change employee settings
        if(!user.getFirstName().isBlank()
                && !user.getLastName().isBlank()
                && !user.getEmail().isBlank()) {
            employeeService.createEmployee(user);
        }

    }

    private User initializeUser(RegisterRequest registerRequest) {
        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .isActive(true)
                .country(registerRequest.getCountry())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

@Cacheable("users")
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DomainException("User with id [%s] does not exist."));
    }

@CacheEvict(value = "users", allEntries = true)
    public void switchStatus(UUID id) {

        User user = getById(id);
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void switchRole(UUID id) {

        User user = getById(id);
        if(user.getRole() == UserRole.USER) {
            user.setRole(UserRole.MANAGER);
        } else {
            user.setRole(UserRole.USER);
        }

        employeeService.saveEmployeeRole(user);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DomainException("User with this username does not exist"));

       return new AuthenticationDetails(
                user.getId(),
                username,
                user.getPassword(),
                user.getRole(),
                user.isActive());
    }

}
