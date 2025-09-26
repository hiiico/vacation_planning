package app.notification.service;

import app.event.EventProducer;
import app.notification.client.NotificationClient;
import app.notification.client.dto.Notification;
import app.notification.client.dto.NotificationPreference;
import app.notification.client.dto.NotificationRequest;
import app.event.payload.UpsertNotificationPreference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Slf4j
@Service
public class NotificationService {
    // Feign client
    private final NotificationClient notificationClient;
    // Kafka
    private final EventProducer eventProducer;

@Autowired
    public NotificationService(NotificationClient notificationClient, EventProducer eventProducer) {
        this.notificationClient = notificationClient;
        this.eventProducer = eventProducer;
}

    public void saveNotificationPreference(UUID userid, boolean isEmailEnabled, String email) {

        UpsertNotificationPreference notificationPreference =
                UpsertNotificationPreference.builder()
                        .userId(userid)
                        .contactInfo(email)
                        .type("EMAIL")
                        .notificationEnabled(isEmailEnabled)
                        .build();
        // http request/response
//        ResponseEntity<Void> httpResponse =
//                notificationClient.upsertNotificationPreferences(notificationPreference);
//        if (!httpResponse.getStatusCode().is2xxSuccessful()) {
//            log.error(
//                    "[Feign call to vacation-planning-notification failed] Can't save user preference for user with id = [%s]"
//                            .formatted(userid));
//        }

        // Kafka Async version that doesn't block the calling thread
        eventProducer.sendUpsertPreference(notificationPreference);
    }

    public NotificationPreference getNotificationPreferences(UUID userId) {
    // http request/response
    ResponseEntity<NotificationPreference> httpResponse = notificationClient.getUserPreference(userId);

    if(!httpResponse.getStatusCode().is2xxSuccessful()) {
        throw new RuntimeException(
                "Notification preference for user id [%s] doesn't exist.".formatted(userId));
    }
    return httpResponse.getBody();
        // TODO Kafka

    }


    public List<Notification> getNotificationHistory(UUID userId) {
        // http request/response
    ResponseEntity<List<Notification>> httpResponse = notificationClient.getNotificationHistory(userId);
    return httpResponse.getBody();
        // TODO Kafka

    }

    public void sendNotification(UUID userId, String emailSubject, String emailBody) {

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(userId)
                .subject(emailSubject)
                .body(emailBody)
                .build();

         //http request/response
//        ResponseEntity<Void> httpResponse;
//        try {
//            httpResponse = notificationClient.sendNotification(notificationRequest);
//            if(!httpResponse.getStatusCode().is2xxSuccessful()) {
//                log.error(
//                        "[Feign call to notification-src failed] Can't send email to user with id = [%s]"
//                                .formatted(userId));
//            }
//
//        } catch (Exception e) {
//            log.error("Can't send email to user with id = [%s] due to 500 Internal Server Error."
//                    .formatted(userId));
//        }

        // Kafka Async version that doesn't block the calling thread
        eventProducer.sendNotification(notificationRequest);
    }

    public void updateNotificationPreference(UUID userId, boolean enabled) {
        //http request/response
    try {
        notificationClient.updateNotificationPreference(userId, enabled);
    } catch (Exception e) {
        log.warn("Can't update notification preference for user with id = [%s].".formatted(userId));
    }
    // TODO Kafka

    }
}
