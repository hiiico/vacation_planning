package app.web;

import app.security.AuthenticationDetails;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    private final UserService employeeService;

    @Autowired
    public IndexController(UserService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String getIndexPage() {
    return "index";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "error", required = false) String errorParam) {

    ModelAndView mnv = new ModelAndView();
    mnv.addObject("loginRequest", new LoginRequest());
    mnv.setViewName("login");

    if(errorParam!= null) {
        mnv.addObject("errorMessage", "Invalid username or password");
    }

    return mnv;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("register");
        mnv.addObject("registerRequest", new RegisterRequest());

        return mnv;
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        User employee = employeeService.getById(authenticationDetails.getUserId());
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("home");
        mnv.addObject("user", employee);

        return mnv;
    }

}
