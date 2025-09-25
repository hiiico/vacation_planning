package app.event;

import app.event.payload.UpsertNotificationPreference;
import app.notification.client.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class EventProducer {

    private final KafkaTemplate<String, EventMessage<?>> kafkaTemplate;
    private final String topic;

    public EventProducer(KafkaTemplate<String, EventMessage<?>> kafkaTemplate,
                         @Value("${app.kafka.input-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    // --- Internal generic send ---
    private <T> void sendAsync(String eventType, String key, T payload) {
        EventMessage<T> message = new EventMessage<>(eventType, payload);
        kafkaTemplate.send(topic, key, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Async sent {} with key={} to topic {}", eventType, key, topic);
                    } else {
                        log.error("Async failed {} with key={}: {}", eventType, key, ex.getMessage());
                    }
                });
    }

    private <T> void sendSync(String eventType, String key, T payload) {
        EventMessage<T> message = new EventMessage<>(eventType, payload);
        try {
            kafkaTemplate.send(topic, key, message).get(); // waits for broker ack
            log.info("Sync sent {} with key={} to topic {}", eventType, key, topic);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Sync send interrupted {} with key={}", eventType, key, e);
        } catch (ExecutionException e) {
            log.error("Sync send failed {} with key={}", eventType, key, e);
        }
    }

    // --- Public API ---
    public void sendUpsertPreference(UpsertNotificationPreference event) {
        sendSync("UPSERT_NOTIFICATION_PREFERENCE", event.getUserId().toString(), event);
    }

    public void sendNotification(NotificationRequest event) {
        sendAsync("NOTIFICATION_REQUEST", event.getUserId().toString(), event);
    }
}
