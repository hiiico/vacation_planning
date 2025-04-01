package app.web;

import app.department.model.Department;
import app.department.service.DepartmentService;
import app.employee.model.Employee;
import app.employee.service.EmployeeService;
import app.web.dto.RegisterDepartmentRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
@Autowired
    public DepartmentController(DepartmentService departmentService, EmployeeService employeeService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    @GetMapping("/register-department")
    public ModelAndView getRegisterDepartmentPage() {

        List<Employee> managers = employeeService.getAllManagers();
        List<String> types = departmentService.getAllDepartmentsTypes();

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("register-department");
        mnv.addObject("registerDepartmentRequest", new RegisterDepartmentRequest());
        mnv.addObject("managers", managers);
        mnv.addObject("types", types);

        return mnv;
    }

    @PostMapping("/register-department")
    public ModelAndView registerNewDepartment(@Valid RegisterDepartmentRequest registerDepartmentRequest,
                                              BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return new ModelAndView("register-department");
        }

        departmentService.register(registerDepartmentRequest);
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("redirect:/home");

        return mnv;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllDepartments() {

        List<Employee> managers = employeeService.getAllManagers();
        List<Department> departments = departmentService.getAllDepartments();

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("departments");
        mnv.addObject("departments", departments);
        mnv.addObject("managers", managers);

        return mnv;
    }

}