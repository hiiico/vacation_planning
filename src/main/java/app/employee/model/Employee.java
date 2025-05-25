package app.employee.model;

import app.contract.model.Contract;
import app.department.model.Department;
import app.vacation.model.Vacation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID employeeId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @OneToOne
    private Contract contract;

    @ManyToOne
    private Department department;

    @Enumerated(EnumType.STRING)
    private Employment employment;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Vacation> vacations = new ArrayList<>();

}
