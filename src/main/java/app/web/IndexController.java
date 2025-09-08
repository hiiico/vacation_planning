package app.web;

import app.security.AuthenticationDetails;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
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

    if(errorParam != null) {
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

    @PostMapping("/register")
    public ModelAndView registerNewUser(@Valid RegisterRequest registerRequest,
                                         BindingResult bindingResult) {

    if(bindingResult.hasErrors()) {
        return new ModelAndView("register");
    }
    userService.register(registerRequest);
    ModelAndView mnv = new ModelAndView();
    mnv.setViewName("redirect:/login");

    return mnv;
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        User user = userService.getById(authenticationDetails.getUserId());
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("home");
        mnv.addObject("user", user);

        return mnv;
    }

}
