//package app.event;
//
//import app.event.payload.UpsertNotificationPreference;
//import app.notification.client.dto.NotificationRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.ExecutionException;
//
//@Component
//@Slf4j
//public class EventProducer {
//
//    private final KafkaTemplate<String, EventMessage<?>> kafkaTemplate;
//    private final String topic;
//
//    public EventProducer(KafkaTemplate<String, EventMessage<?>> kafkaTemplate,
//                         @Value("${app.kafka.input-topic}") String topic) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.topic = topic;
//    }
//
//    // --- Internal generic send ---
//    private <T> void sendAsync(String eventType, String key, T payload) {
//        EventMessage<T> message = new EventMessage<>(eventType, payload);
//        kafkaTemplate.send(topic, key, message)
//                .whenComplete((result, ex) -> {
//                    if (ex == null) {
//                        log.info("Async sent {} with key={} to topic {}", eventType, key, topic);
//                    } else {
//                        log.error("Async failed {} with key={}: {}", eventType, key, ex.getMessage());
//                    }
//                });
//    }
//
//    private <T> void sendSync(String eventType, String key, T payload) {
//        EventMessage<T> message = new EventMessage<>(eventType, payload);
//        try {
//            kafkaTemplate.send(topic, key, message).get(); // waits for broker ack
//            log.info("Sync sent {} with key={} to topic {}", eventType, key, topic);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            log.error("Sync send interrupted {} with key={}", eventType, key, e);
//        } catch (ExecutionException e) {
//            log.error("Sync send failed {} with key={}", eventType, key, e);
//        }
//    }
//
//    // --- Public API ---
//    public void sendUpsertPreference(UpsertNotificationPreference event) {
//        sendSync("UPSERT_NOTIFICATION_PREFERENCE", event.getUserId().toString(), event);
//    }
//
//    public void sendNotification(NotificationRequest event) {
//        sendAsync("NOTIFICATION_REQUEST", event.getUserId().toString(), event);
//    }
//}

package app.event;

import app.event.payload.UpsertNotificationPreference;
import app.notification.client.dto.NotificationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class EventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public EventProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            @Value("${app.kafka.input-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    // --- Internal generic send methods that match consumer format ---
    private <T> void sendAsync(String eventType, String key, T payload) {
        try {
            String jsonMessage = createMessage(eventType, payload);
            kafkaTemplate.send(topic, key, jsonMessage)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Async sent {} with key={} to topic {}", eventType, key, topic);
                        } else {
                            log.error("Async failed {} with key={}: {}", eventType, key, ex.getMessage());
                        }
                    });
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize {} message with key={}: {}", eventType, key, e.getMessage());
        }
    }

    private <T> void sendSync(String eventType, String key, T payload) {
        try {
            String jsonMessage = createMessage(eventType, payload);
            kafkaTemplate.send(topic, key, jsonMessage).get(); // waits for broker ack
            log.info("Sync sent {} with key={} to topic {}", eventType, key, topic);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Sync send interrupted {} with key={}", eventType, key, e);
        } catch (ExecutionException e) {
            log.error("Sync send failed {} with key={}", eventType, key, e);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize {} message with key={}: {}", eventType, key, e.getMessage());
        }
    }

    // Create JSON message in the EXACT format expected by the consumer
    private <T> String createMessage(String eventType, T payload) throws JsonProcessingException {
        EventMessage<T> message = new EventMessage<>(eventType, payload);
        return objectMapper.writeValueAsString(message);
    }

    // --- Public API (unchanged interface) ---
    public void sendUpsertPreference(UpsertNotificationPreference event) {
        sendSync("UPSERT_NOTIFICATION_PREFERENCE", event.getUserId().toString(), event);
    }

    public void sendNotification(NotificationRequest event) {
        sendAsync("NOTIFICATION_REQUEST", event.getUserId().toString(), event);
    }

    // Optional: Add method without key for flexibility
    public void sendNotification(NotificationRequest event, boolean useKey) {
        if (useKey) {
            sendAsync("NOTIFICATION_REQUEST", event.getUserId().toString(), event);
        } else {
            sendAsync("NOTIFICATION_REQUEST", null, event);
        }
    }

    // EventMessage class that EXACTLY matches what the consumer expects
    public static class EventMessage<T> {
        private String eventType;
        private T payload;
        private String timestamp;
        private String messageId;

        public EventMessage() {
            this.timestamp = java.time.Instant.now().toString();
            this.messageId = java.util.UUID.randomUUID().toString();
        }

        public EventMessage(String eventType, T payload) {
            this();
            this.eventType = eventType;
            this.payload = payload;
        }

        // Getters and setters (must match consumer's expected structure)
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }

        public T getPayload() { return payload; }
        public void setPayload(T payload) { this.payload = payload; }

        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

        public String getMessageId() { return messageId; }
        public void setMessageId(String messageId) { this.messageId = messageId; }
    }
}
