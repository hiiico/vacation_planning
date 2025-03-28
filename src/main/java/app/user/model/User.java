package app.user.model;

import app.department.model.Department;
import app.vacation.model.Vacation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Country country;

    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private Employment employment;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @OneToMany
    private List<Vacation> vacations;

    @OneToOne
    private Department department;

    public List<Vacation> getVacations() {
        //TODO
        return null;
    }

    public void setDepartment(Department department) {
        //TODO
    }

}
