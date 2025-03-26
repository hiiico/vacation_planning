package app.notification.service;

import app.notification.client.NotificationClient;
import app.notification.client.dto.Notification;
import app.notification.client.dto.NotificationPreference;
import app.notification.client.dto.UpsertNotificationPreference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class NotificationService {

    private final NotificationClient notificationClient;
@Autowired
    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void saveNotificationPreference(UUID userid, boolean isEmailEnabled, String email) {

        UpsertNotificationPreference notificationPreference =
                UpsertNotificationPreference.builder()
                        .userId(userid)
                        .contactInfo(email)
                        .type("EMAIL")
                        .notificationEnabled(isEmailEnabled)
                        .build();
//Invoke Fein client and execute HTTP Post Request
        ResponseEntity<Void> httpResponse =
                notificationClient.upsertNotificationPreferences(notificationPreference);

        if (!httpResponse.getStatusCode().is2xxSuccessful()) {
            log.error(
                    "[Feign call to vacation-planning-notification failed] Can't save user preference for user with id = [%s]"
                            .formatted(userid));
        }
    }

    public NotificationPreference getNotificationPreferences(UUID userId) {

    ResponseEntity<NotificationPreference> httpResponse = notificationClient.getUserPreference(userId);

    if(!httpResponse.getStatusCode().is2xxSuccessful()) {
        throw new RuntimeException(
                "Notification preference for user id [%s] doesn't exist.".formatted(userId));
    }
    return httpResponse.getBody();
    }

    public List<Notification> getNotificationHistory(UUID userId) {
    ResponseEntity<List<Notification>> httpResponse = notificationClient.getNotificationHistory(userId);
    return httpResponse.getBody();
    }
}
