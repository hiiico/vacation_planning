package app.notification.service;

import app.notification.employee.NotificationClient;
import app.notification.employee.dto.NotificationPreference;
import app.notification.employee.dto.UpsertNotificationPreference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class NotificationService {

    private final NotificationClient notificationClient;
@Autowired
    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

//    @Async
//    @EventListener
//    public void sendEmailWhenChargeHappen(PaymentNotificationEvent event) {
//        System.out.printf("Thread [%s]: Charge happened for user with id [%s]", Thread.currentThread(), event.getUserId());
//    }

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
                    "[Feign call to notification-src failed] Can't save user preference for user with id = [%s]"
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
}
