package app.web;

import app.employee.model.Employee;
import app.employee.service.EmployeeService;
import app.security.AuthenticationDetails;
import app.user.model.User;
import app.user.service.UserService;
import app.vacation.model.Vacation;
import app.vacation.service.VacationService;
import app.web.dto.VacationEditRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;


@Controller
@RequestMapping("/vacations")
public class VacationController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final VacationService vacationService;

    public VacationController(UserService userService, EmployeeService employeeService, VacationService vacationService) {
        this.userService = userService;
        this.employeeService = employeeService;
        this.vacationService = vacationService;
    }

    @GetMapping("/register-vacation")

    public ModelAndView getCreateRequest(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        User user = userService.getById(authenticationDetails.getUserId());
        Employee employee = employeeService.getByUsername(user.getUsername());

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("register-vacation");
        mnv.addObject("employee", employee);
        mnv.addObject("vacationEditRequest", new VacationEditRequest());

        return mnv;
    }

    @PostMapping("/register-vacation")
    public ModelAndView registerNewVacation(@Valid VacationEditRequest vacationEditRequest,
                                        BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return new ModelAndView("register-vacation");
        }

        vacationService.createVacation(vacationEditRequest);
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("redirect:/home");

        return mnv;
    }

}