package app.notification.client;

import app.notification.client.dto.Notification;
import app.notification.client.dto.NotificationPreference;
import app.notification.client.dto.NotificationRequest;
import app.notification.client.dto.UpsertNotificationPreference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
// docker-container
//@FeignClient(name = "urlaubsplanung-notification", url="http://urlaubsplanung-notification:8081/api/v1/notifications")
//@FeignClient(name = "vacation-planning-notifications", url="http://localhost:8081/api/v1/notifications")
public interface NotificationClient {

    @PostMapping("/preferences")
    ResponseEntity<Void> upsertNotificationPreferences(
            @RequestBody UpsertNotificationPreference notificationPreference);

    @GetMapping("/preferences")
    ResponseEntity<NotificationPreference> getUserPreference(@RequestParam(name="userId") UUID userId);

    @GetMapping
    ResponseEntity<List<Notification>> getNotificationHistory(
            @RequestParam(name = "userId") UUID userId);

    @PostMapping
    ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest notificationRequest);

    @PutMapping("/preferences")
    ResponseEntity<Void> updateNotificationPreference(@RequestParam(name ="userId") UUID userId, @RequestParam(name = "enable") boolean enable);

}
