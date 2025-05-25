package app.web;

import app.exception.CanNotChangeDetails;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.UserEditRequest;
import app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/profile")
    public ModelAndView getProfileMenu(@PathVariable UUID id) {

        User user = userService.getById(id);
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("profile-menu");
        mnv.addObject("user", user);
        mnv.addObject("userEditRequest", DtoMapper.mapUserToUserEditRequest(user));
        return mnv;
    }

    @PutMapping("/{id}/profile")
    public ModelAndView updateUserProfile(@PathVariable UUID id,
                                          @Valid UserEditRequest userEditRequest,
                                          BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            User user = userService.getById(id);
            ModelAndView mnv = new ModelAndView();
            mnv.setViewName("profile-menu");
            mnv.addObject("user", user);
            mnv.addObject("userEditRequest", userEditRequest);
            return mnv;
        }
        userService.editUserDetails(id, userEditRequest);

        return new ModelAndView("redirect:/home");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllUsers() {

        List<User> users = userService.getAllUser();
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("users");
        mnv.addObject("users", users);
        return mnv;
    }

    @PutMapping("/{id}/status")
    public String switchUserProfile(@PathVariable UUID id) {

        userService.switchStatus(id);
        return "redirect:/users";
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public String switchUserRole(@PathVariable UUID id) {

        userService.switchRole(id);
        return "redirect:/users";
    }

    @ExceptionHandler(CanNotChangeDetails.class)
    public ModelAndView handelCanNotChangeDetails() {

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("home");

        return mnv;
    }

}
