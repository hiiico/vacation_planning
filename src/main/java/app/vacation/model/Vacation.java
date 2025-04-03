package app.vacation.model;

import app.employee.model.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Employee employee;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private AbsencesReason absencesReason;

    @Column(nullable = false)
    private boolean approved;

    private int absenceDays;

}