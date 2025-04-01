package app.contract.model;

import app.department.model.Department;
import app.employee.model.Employee;
import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ContractType type;

    @OneToOne
    private Employee employee;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int leaveDays;

    @Column(nullable = false)
    boolean renewalAllowed;

    boolean active;

}
