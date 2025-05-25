package app.web;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerMvcApiTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void putUnauthorisedRequestToSwitchRole_shouldReturn404AndNotFoundView() throws Exception {
//
//        // build  authenticated user for authenticated request
//        AuthenticationDetails user = new AuthenticationDetails(
//                UUID.randomUUID(), "user123",
//                "123123",
//                UserRole.USER,
//                true);
//                                                                    // pathVariable for id-> random
//        MockHttpServletRequestBuilder request = put("/users/{id}/role", UUID.randomUUID())
//                .with(user(user))
//                .with(csrf());
//
//        mockMvc.perform(request)
//                .andExpect(status().isNotFound())
//                .andExpect(view().name("not-found"));
    }

    @Test
    void putAuthorisedRequestToSwitchRole_shouldRedirectToUsers() throws Exception {

        // build  authenticated user for authenticated request
        AuthenticationDetails user = new AuthenticationDetails(
                UUID.randomUUID(), "user123",
                "123123",
                UserRole.ADMIN,
                true);
        // pathVariable for id-> random
        MockHttpServletRequestBuilder request = put("/users/{id}/role", UUID.randomUUID())
                .with(user(user))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
        verify(userService, times(1)).switchRole(any());
    }
}
