package app.web;

import app.vacation.model.Vacation;
import app.vacation.service.VacationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/vacations")
public class VacationController {

    private final VacationService vacationService;

    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping("/user/{userId}")
    public String listVacationsByUser(@PathVariable UUID userId, Model model) {
        model.addAttribute("vacations", vacationService.getVacationsByUserId(userId));
        model.addAttribute("userId", userId);
        return "vacations/list";
    }

    @GetMapping("/create/{userId}")
    public String showCreateForm(@PathVariable UUID userId, Model model) {
        model.addAttribute("vacation", new Vacation());
        model.addAttribute("userId", userId);
        return "vacations/create";
    }

    @PostMapping("/create/{userId}")
    public String createVacation(@PathVariable UUID userId, @ModelAttribute Vacation vacation) {
        vacationService.createVacation(userId, vacation.getDestination().getId(), vacation);
        return "redirect:/vacations/user/" + userId;
    }

    @GetMapping("/delete/{id}")
    public String deleteVacation(@PathVariable UUID id) {
        UUID userId = vacationService.getVacationsByUserId(id).get(0).getUser().getId();
        vacationService.deleteVacation(id);
        return "redirect:/vacations/user/" + userId;
    }
}