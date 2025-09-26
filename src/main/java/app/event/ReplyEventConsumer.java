package app.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReplyEventConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${app.kafka.reply-topic}",
            groupId = "main-app-replies"
    )
    public void consumeReply(@Payload String message) {
        try {
            JsonNode eventMessage = objectMapper.readTree(message);
            String eventType = eventMessage.path("eventType").asText(null);
            JsonNode payload = eventMessage.path("payload");

            if (eventType == null) {
                log.error("Reply event missing eventType: {}", message);
                return;
            }

            switch (eventType) {
                case "NOTIFICATION_PREFERENCE_RESPONSE" -> handlePreferenceResponse(payload);
                case "NOTIFICATION_RESPONSE" -> handleNotificationResponse(payload);
//                case "PASSWORD_RESET_RESPONSE" -> handlePasswordResetResponse(payload);
                default -> log.warn("Unknown reply eventType [{}]: {}", eventType, message);
            }

        } catch (Exception e) {
            log.error("Failed to process reply message: {}", message, e);
        }
    }

    private void handlePreferenceResponse(JsonNode payload) {
        try {
            String userIdText = payload.path("userId").asText(null);
            boolean success = payload.path("success").asBoolean(false);

            if (userIdText == null) {
                log.error("Reply payload missing userId: {}", payload);
                return;
            }

            UUID userId = UUID.fromString(userIdText);

            if (success) {
                //TODO reacting programmatically -> Update DB or state accordingly

                log.info("User [{}] preferences saved successfully", userId);
            } else {
                // TODO handle exception
                String error = payload.path("error").asText("Unknown error");
                log.error("Failed to save preferences for user [{}]: {}", userId, error);
            }

        } catch (Exception e) {
            log.error("Failed to process NOTIFICATION_PREFERENCE_RESPONSE: {}", payload, e);
        }
    }

    private void handleNotificationResponse(JsonNode payload) {
        try {
            String userIdText = payload.path("userId").asText(null);
            boolean success = payload.path("success").asBoolean(false);

            if (userIdText == null) {
                log.error("Reply payload missing userId: {}", payload);
                return;
            }

            UUID userId = UUID.fromString(userIdText);

            if (success) {
                //TODO reacting programmatically -> Update DB or state accordingly

                log.info("User [{}] notification send successfully", userId);
            } else {
                // TODO handle exception
                String error = payload.path("error").asText("Unknown error");
                log.error("Failed to send notification to user [{}]: {}", userId, error);
            }

        } catch (Exception e) {
            log.error("Failed to process NOTIFICATION_RESPONSE: {}", payload, e);
        }
    }

}
