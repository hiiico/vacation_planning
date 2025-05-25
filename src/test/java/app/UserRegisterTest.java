package app;

import app.employee.model.Employee;
import app.employee.repository.EmployeeRepository;
import app.employee.service.EmployeeService;
import app.user.model.Country;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest // integration test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)//clean H2 database
public class UserRegisterTest {

//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private EmployeeService employeeService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;

 /*   @BeforeEach // clean MYSQL database
    void clear() {

        userRepository.deleteAll();
        employeeRepository.deleteAll();
    }
  */
//    @Test
//    void registerUser() {
//
//        // given
//        RegisterRequest registerRequest = RegisterRequest.builder()
//                .username("admin")
//                .password("123123")
//                .country(Country.BULGARIA)
//                .build();
//        User user = userService.register(registerRequest);
//
//        UserEditRequest userEditRequest = UserEditRequest.builder()
//                .firstName("Hristo")
//                .lastName("Ivanov")
//                .email("hristo@mail.com")
//                .build();
//        userService.editUserDetails(user.getId(),userEditRequest);
//        Employee employee = employeeService.getByUsername(user.getUsername());
//        // when
//        employeeService.createEmployee(user);
//
//        // then
//        assertThat(user.getUsername()).isEqualTo(registerRequest.getUsername());
//        // or
//        // assertEquals(user.getUsername(), registerRequest.getUsername());
//        assertThat(user.getUsername()).isEqualTo(employee.getUsername());
//        // or
//        assertEquals(user.getFirstName(), employee.getFirstName());
//    }
}
