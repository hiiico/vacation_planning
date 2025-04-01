package app.web;

import app.employee.model.Employee;
import app.employee.service.EmployeeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ModelAndView getAllEmployees() {

        List<Employee> employees = employeeService.getAllEmployees();
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("employees");
        mnv.addObject("employees", employees);
            return mnv;
        }

        @PutMapping("/{id}/employment")
        public String switchEmployeeEmployment(@PathVariable UUID id) {

            employeeService.switchEmployment(id);
            return "redirect:/employees";
        }

}
