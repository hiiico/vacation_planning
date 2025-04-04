package app.contract.repository;

import app.contract.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {

    List<Contract> getAllByRenewalAllowed(boolean renewalAllowed);

    List<Contract> findByEmployee_EmployeeId(UUID employeeId);

}
