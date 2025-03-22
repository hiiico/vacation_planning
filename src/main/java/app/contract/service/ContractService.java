package app.contract.service;

import app.contract.model.Contract;
import app.contract.model.ContractType;
import app.contract.repository.ContractRepository;
import app.department.model.Department;
import app.user.model.User;
import app.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class ContractService {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }


    public Contract createDefaultContract(User user, Department department) {

        Contract contract = contractRepository.save(initilizeContract(user, department));

        log.info("Successfully created new contract with name [%s] and type [%s]."
                .formatted(user.getUsername(), department.getType()));
        return contract;
    }

    private Contract initilizeContract(User user, Department department) {

        return Contract.builder()
                .user(user)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(6))
                .type(ContractType.TEMPORARY)
                .renewalAllowed(false)
                .active(true)
                .build();
    }

//    public Contract createContract(UUID userId, Contract contract) {
//        User user = userService.getById(userId);
//                //.orElseThrow(() -> new RuntimeException("User not found"));
//        contract.setUser(user);
//        return contractRepository.save(contract);
//    }

    public List getContractsByUserId(UUID userId) {
        return (List) contractRepository.findByUserId(userId);
    }

    public void deleteContract(UUID id) {
        contractRepository.deleteById(id);
    }
}
