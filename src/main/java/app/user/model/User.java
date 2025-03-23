package app.user.model;

import app.contract.model.Contract;
import app.department.model.Department;
import app.vacation.model.Vacation;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    private String firstName;

    private String lastName;

    private String profilePicture;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToOne
    private Department department;

    @OneToMany
    private List<Vacation> vacations = new ArrayList<>();

    @OneToOne
    private Contract contract;

    private boolean isActive;

}
