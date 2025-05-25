package app.vacation.service;

import app.employee.model.Employee;
import app.employee.service.EmployeeService;
import app.user.service.UserService;
import app.vacation.model.Vacation;
import app.vacation.repository.VacationRepository;
import app.web.dto.VacationEditRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@Service
public class VacationService {

    private final VacationRepository vacationRepository;
    private final EmployeeService employeeService;
    private final UserService userService;

    public VacationService(VacationRepository vacationRepository, EmployeeService employeeService, UserService userService) {
        this.vacationRepository = vacationRepository;
        this.employeeService = employeeService;
        this.userService = userService;
    }

    public void createVacation(VacationEditRequest vacationEditRequest) {

        Optional<Vacation> vacationOptional = vacationRepository.findByEmployee_EmployeeId(vacationEditRequest.getEmployee().getEmployeeId());

        if(vacationOptional.isEmpty()) {
            vacationRepository.save(initializeVacation(vacationEditRequest));
        }

    }

    private Vacation initializeVacation(VacationEditRequest vacationEditRequest) {

        long days = getWorkingDays(vacationEditRequest.getStartDate(), vacationEditRequest.getEndDate());

        return Vacation.builder()
                .employee(vacationEditRequest.getEmployee())
                .startDate(vacationEditRequest.getStartDate())
                .endDate(vacationEditRequest.getEndDate())
                .absencesReason(vacationEditRequest.getAbsencesReason())
                .approved(false)
                .absenceDays((int) days + 1)
                .build();
    }

    private long getWorkingDays(LocalDate startDate, LocalDate endDate) {

            return startDate.datesUntil(endDate)
                    .map(LocalDate::getDayOfWeek)
                    .filter(day -> !Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(day))
                    .count();
        }

}
