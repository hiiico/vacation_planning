package app.contract.service;

import app.contract.model.Contract;
import app.contract.model.ContractType;
import app.contract.repository.ContractRepository;
import app.department.model.Department;
import app.employee.model.Employee;
import app.employee.service.EmployeeService;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.RegisterContractRequest;
import app.web.dto.RegisterDepartmentRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ContractService {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public void createContract(Employee employee, Department department) {

       contractRepository.save(initilizeContract(employee, department));


        log.info("Successfully created new contract with employee [%s] [%s] in [%s] department."
                .formatted(employee.getFirstName(), employee.getLastName(), department.getName()));
    }

    private Contract initilizeContract(Employee employee, Department department) {

        return Contract.builder()
                .type(ContractType.TEMPORARY)
                .employee(employee)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(6))
                .description("New Contract")
                .renewalAllowed(false)
                .active(true)
                .build();
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public List<Contract> getContract(UUID employeeId) {

        return contractRepository.findByEmployee_EmployeeId(employeeId);
    }

    public void register( RegisterContractRequest registerContractRequest) {

            Contract contract = initializeContract(registerContractRequest);
            contractRepository.save(contract);
        }

        private Contract initializeContract(RegisterContractRequest registerContractRequest) {
            return Contract.builder()
                    .type(ContractType.TEMPORARY)
                    .employee(registerContractRequest.getEmployee())
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusMonths(6))
                    .description("New Contract")
                    .leaveDays(12)
                    .renewalAllowed(false)
                    .active(true)
                    .build();
        }
    }

