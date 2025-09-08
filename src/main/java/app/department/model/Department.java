package app.department.model;

import app.employee.model.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepartmentType type;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Employee> employees = new ArrayList<>();

    @OneToOne
    private Employee manager;

}
