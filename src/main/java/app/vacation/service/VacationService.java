package app.vacation.service;

import app.user.model.User;
import app.user.service.UserService;
import app.vacation.model.Vacation;
import app.vacation.repository.VacationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VacationService {

    private final VacationRepository vacationRepository;
    private final UserService userService;

    public VacationService(VacationRepository vacationRepository, UserService userService) {
        this.vacationRepository = vacationRepository;
        this.userService = userService;
    }

    public Vacation createVacation(UUID userId,Vacation vacation) {
       //TODO
        return null;
    }

    public List<Vacation> getVacationsByUserId(UUID userId) {
        return vacationRepository.findByUserId(userId);
    }

    public void deleteVacation(UUID id) {
        //TODO
    }
}
