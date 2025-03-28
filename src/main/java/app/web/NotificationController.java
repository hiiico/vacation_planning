package app.web;

import app.notification.client.dto.Notification;
import app.notification.client.dto.NotificationPreference;
import app.notification.service.NotificationService;
import app.security.AuthenticationDetails;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(UserService employeeService, NotificationService notificationService) {
        this.userService = employeeService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public ModelAndView getNotificationPage(
            @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        User user = userService.getById(authenticationDetails.getUserId());

        NotificationPreference notificationPreference = notificationService.getNotificationPreferences(user.getId());
        List<Notification> notificationHistory = notificationService.getNotificationHistory(user.getId());
        long succeededNotifications = notificationHistory.stream()
                .filter(notification -> notification.getStatus().equals("SUCCEEDED"))
                .count();
        long failedNotifications = notificationHistory.stream()
                .filter(notification -> notification.getStatus().equals("FAILED"))
                .count();
        notificationHistory = notificationHistory.stream()
                .limit(10)
                .toList();

        ModelAndView mnv = new ModelAndView("notifications");
        mnv.addObject("user", user);
        mnv.addObject("notificationPreference", notificationPreference);
        mnv.addObject("succeededNotifications", succeededNotifications);
        mnv.addObject("failedNotifications", failedNotifications);
        mnv.addObject("notificationHistory", notificationHistory);

        return mnv;
    }

    @PutMapping("/user-preference")
    public String updateUserPreference(
            @RequestParam(name = "enabled") boolean enabled,
            @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        notificationService.updateNotificationPreference(authenticationDetails.getUserId(), enabled);

        return "redirect:/notifications";
    }
}
