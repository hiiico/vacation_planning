package app.web;

import app.exception.UsernameAlreadyExist;
import app.security.AuthenticationDetails;
import app.user.model.UserRole;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static app.TestBuilder.aRandomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IndexController.class)
public class IndexControllerMvcApiTest {

    // When I test controllers I have to Mock all dependencies with annotation @MockitoBean
    @MockitoBean
    private UserService userService;
    // use MockMvc to send request
    @Autowired
    private MockMvc mockMvc;
    // Send GET /
    // Result - view name index
    @Test
    void getRequestToIndexEndpointShouldReturnIndexView() throws Exception{

        // build request
        MockHttpServletRequestBuilder request = get("/");
        // .andExpect() - check result
        // MockMvcResultMatchers.status() - check status
        // send request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void getRequestToRegisterEndPoint_shouldReturnRegisterView() throws Exception {

        MockHttpServletRequestBuilder request = get("/register");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
        //        .andExpect(model().attribute("registerRequest", instanceOf(RegisterRequest.class)))
                .andExpect(model().attributeExists("registerRequest"));
    }

    @Test
    void getRequestToLoginEndPoint_shouldReturnLoginView() throws Exception {

        MockHttpServletRequestBuilder request = get("/login").param("error", "");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest", "errorMessage"));
    }

    @Test
    void postRequestToRegisterEndPoint_shouldReturnHappyPath() throws Exception {

        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "administrator")
                .formField("password", "Hristo1972")
                .formField("country", "BULGARIA")
                // The csrf() static method is provided by dependency-> spring-security-test
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        verify(userService, times(1)).register(any());
    }

    @Test
    void postRequestToRegisterEndPointWhenUserAlreadyExist_thenRedirectToRegisterWithFlashParameter() throws Exception {

//        // mock exception
//        when(userService.register(any())).thenThrow(new UsernameAlreadyExist("Username already exist!"));
//        MockHttpServletRequestBuilder request = post("/register")
//                .formField("username", "administrator")
//                .formField("password", "Hristo1972")
//                .formField("country", "GERMANY")
//                .with(csrf());
//
//
//        mockMvc.perform(request)
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/register"))
//                .andExpect(flash().attributeExists("usernameAlreadyExistMessage"));
//        verify(userService, times(1)).register(any());
    }

    @Test
    void postRequestRegisterEndPointWithInvalidData_returnRegisterView() throws Exception {

        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "")
                .formField("password", "")
                .formField("country", "BULGARIA")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
        verify(userService, never()).register(any());
    }

    // home endpoint need security test dependence for Authorisation and Identification
    @Test
    void getAuthenticatedRequestToHome_returnHomeView() throws Exception {

        // build aRandomUser here or in TestBuilder class
        /* User aRandomUser = User.builder()
                .id(UUID.randomUUID())
                .username("administrator")
                .password("Hristo1972")
                .role(UserRole.ADMIN)
                .country(Country.BULGARIA)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        */

        when(userService.getById(any())).thenReturn(aRandomUser());

        UUID userId = UUID.randomUUID();
        // build  authenticated user for authenticated request
        AuthenticationDetails user = new AuthenticationDetails(
                userId,
                "administrator",
                "Hristo1972",
                UserRole.ADMIN,
                true
        );

        // build request
        MockHttpServletRequestBuilder request = get("/home")
                .with(user(user));

        // send request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("user"));
        verify(userService, times(1)).getById(userId);
    }

    @Test
    void getUnauthenticatedRequestToHome_redirectLogin() throws Exception {

//        MockHttpServletRequestBuilder request = get("home");
//
//        mockMvc.perform(request)
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("https://localhost:8080/login"));
//        verify(userService, never()).getById(any());
    }

}
