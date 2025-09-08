package app.scheduler;

import app.contract.model.Contract;
import app.contract.model.ContractType;
import app.contract.repository.ContractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class ContractRenewal {

    private final ContractRepository contractService;

    @Autowired
    public ContractRenewal(ContractRepository contractService) {
        this.contractService = contractService;
    }

    //@Scheduled(fixedDelay = 86400)
    //@Scheduled(cron = "0 0 0 1 * *")
    @Scheduled(cron = "@daily")

    public void renewalContract() {

        List<Contract> contracts = contractService.getAllByRenewalAllowed(true);

        if(contracts.isEmpty()) {
            log.info("No contracts to renewal.");
            return;
        }

        for (Contract contract : contracts) {
            if(contract.getType() == ContractType.PERMANENT) {
                contract.setEndDate(LocalDate.now().plusYears(1));

            } else {
                contract.setActive(false);
            }
        }
    }
}
