package app.vacation.model;

import app.destination.model.Destination;
import app.user.model.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    public void setUser(User user) {
        this.user = user;
        user.getVacations().add(this);
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
        destination.getVacations().add(this);
    }
}