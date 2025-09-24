package app.event;

import app.event.payload.UpsertNotificationPreference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

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

    public <T> void send(String eventType, String key, T payload) {
        EventMessage<T> message = new EventMessage<>(eventType, payload);

        kafkaTemplate.send(topic, key, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent {} with key={} to topic {}", eventType, key, topic);
                    } else {
                        log.error("Failed to send {} with key={}: {}", eventType, key, ex.getMessage());
                    }
                });
    }

    public void sendUpsertPreference(UpsertNotificationPreference event) {
        send("UPSERT_NOTIFICATION_PREFERENCE", event.getUserId().toString(), event);
    }

    // add more cases as your app grows

//    public void sendUserRegistered(UserRegisteredEvent event) {
//        send("USER_REGISTERED", event.getUserId().toString(), event);
//    }
//
//    public void sendNotification(NotificationEvent event) {
//        send("NOTIFICATION", event.getTargetUser().toString(), event);
//    }

}

