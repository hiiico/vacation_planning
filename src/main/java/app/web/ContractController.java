package app.web;

import app.contract.model.Contract;
import app.contract.service.ContractService;
import app.department.model.Department;
import app.employee.model.Employee;
import app.employee.service.EmployeeService;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.RegisterContractRequest;
import app.web.dto.RegisterDepartmentRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;
    private final EmployeeService employeeService;
    private final UserRepository userRepository;
    private final UserService userService;

    public ContractController(ContractService contractService, EmployeeService employeeService, UserRepository userRepository, UserService userService) {
        this.contractService = contractService;
        this.employeeService = employeeService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/register-contract")
    public ModelAndView getRegisterContractPage() {

        List<Employee> managers = employeeService.getAllManagers();

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("register-contract");
        mnv.addObject("registerContractRequest", new RegisterContractRequest());
        mnv.addObject("managers", managers);

        return mnv;
    }

//    @PostMapping("/register-contract")
//    public ModelAndView registerNewDepartment(@Valid RegisterContractRequest registerContractRequest,
//                                              BindingResult bindingResult) {
//
//        if(bindingResult.hasErrors()) {
//            return new ModelAndView("register-contract");
//        }
//
//        contractService.register(registerContractRequest);
//        ModelAndView mnv = new ModelAndView();
//        mnv.setViewName("redirect:/home");
//
//        return mnv;
//    }


    @GetMapping("/{id}")

public ModelAndView getContract(@PathVariable UUID id) {

        User user = userService.getById(id);
        Employee employee = employeeService.getByUsername(user.getUsername());
        List<Contract> contracts = contractService.getContract(employee.getEmployeeId());

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("contracts");
        mnv.addObject("contracts", contracts);

        return mnv;
}
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllContracts() {

        List<Contract> contracts = contractService.getAllContracts();

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("contracts");
        mnv.addObject("contracts", contracts);

        return mnv;
    }


}