package app.web;

import app.notification.employee.dto.NotificationPreference;
import app.notification.service.NotificationService;
import app.security.AuthenticationDetails;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final UserService employeeService;
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(UserService employeeService, NotificationService notificationService) {
        this.employeeService = employeeService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public ModelAndView getNotificationPage(
            @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        User user = employeeService.getById(authenticationDetails.getUserId());

        NotificationPreference notificationPreference = notificationService.getNotificationPreferences(user.getId());
        ModelAndView mnv = new ModelAndView("notifications");
        mnv.addObject("user", user);
        mnv.addObject("notificationPreferences", notificationPreference);

        return mnv;
    }
}
