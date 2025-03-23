package app.notification.employee;

import app.notification.employee.dto.NotificationPreference;
import app.notification.employee.dto.UpsertNotificationPreference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "vacation-planning-notifications", url="http://localhost:8081/api/v1/notifications")
public interface NotificationClient {

    @PostMapping("/preferences")
    ResponseEntity<Void> upsertNotificationPreferences(
            @RequestBody UpsertNotificationPreference notificationPreference);

    @GetMapping("/preferences")
    ResponseEntity<NotificationPreference> getUserPreference(@RequestParam(name="userId") UUID userId);
}
