package app.user.service;

import app.exception.DomainException;
import app.exception.UsernameAlreadyExist;
import app.notification.service.NotificationService;
import app.security.AuthenticationDetails;
import app.user.model.Employment;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
    }


    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public User register(RegisterRequest registerRequest) {

        Optional<User> userOptional = userRepository.findByUsername(registerRequest.getUsername());

        if(userOptional.isPresent()) {
            throw new UsernameAlreadyExist("Username [%s] already exist.".formatted(registerRequest.getUsername()));
        }

        User user = userRepository.save(initializeUser(registerRequest));

        notificationService.saveNotificationPreference(user.getId(), false, null);


        log.info("Successfully created new user account for username [%s] and id [%s]"
                .formatted(registerRequest.getUsername(), user.getId()));
        return user;
    }

    @CacheEvict(value = "users", allEntries = true)
    public void editUserDetails(UUID userId, UserEditRequest userEditRequest) {

        User user = getById(userId);

        if(userEditRequest.getEmail().isBlank()) {

            String emailBody = "Hello %s, your contact email %s has been successfully removed from the Vacation Planner app."
                    .formatted(user.getUsername(), user.getEmail());
            notificationService.sendNotification(user.getId(), "Removed email.", emailBody);

            notificationService.saveNotificationPreference(user.getId(), false, null);
        }

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setEmail(userEditRequest.getEmail());
        user.setProfilePicture(userEditRequest.getProfilePicture());

        if(!userEditRequest.getEmail().isBlank()) {


            notificationService.saveNotificationPreference(user.getId(), true, userEditRequest.getEmail());

            String emailBody = "Hello %s, your contact email %s has been successfully added to the Vacation Planner app."
                    .formatted(user.getUsername(), user.getEmail());
            notificationService.sendNotification(user.getId(), "Added email.", emailBody);
        }

        userRepository.save(user);
    }

    private User initializeUser(RegisterRequest registerRequest) {
        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .isActive(true)
                .employment(null)
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

    public void switchEmployment(UUID id) {

        User user = getById(id);
        if(user.getEmployment() == Employment.ENGAGED) {
            user.setEmployment(Employment.VACATION);
        } else {
            user.setEmployment(Employment.ENGAGED);
        }
        userRepository.save(user);
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

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DomainException("User with this username does not exist"));

       return new AuthenticationDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.isActive());
    }


}
