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
//                case "USER_REGISTER_RESPONSE" -> handleRegisterResponse(payload);
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
                log.info("User [{}] preferences saved successfully", userId);
            } else {
                String error = payload.path("error").asText("Unknown error");
                log.error("Failed to save preferences for user [{}]: {}", userId, error);
            }

        } catch (Exception e) {
            log.error("Failed to process NOTIFICATION_PREFERENCE_RESPONSE: {}", payload, e);
        }
    }

//    private void handleRegisterResponse(JsonNode payload) {
//        try {
//            String userIdText = payload.path("userId").asText(null);
//            boolean success = payload.path("success").asBoolean(false);
//
//            if (userIdText == null) {
//                log.error("Register reply missing userId: {}", payload);
//                return;
//            }
//
//            if (success) {
//                log.info("User [{}] registered successfully", userIdText);
//            } else {
//                String error = payload.path("error").asText("Unknown error");
//                log.error("User [{}] registration failed: {}", userIdText, error);
//            }
//
//        } catch (Exception e) {
//            log.error("Failed to process USER_REGISTER_RESPONSE: {}", payload, e);
//        }
//    }
//
//    private void handlePasswordResetResponse(JsonNode payload) {
//        try {
//            String userIdText = payload.path("userId").asText(null);
//            boolean success = payload.path("success").asBoolean(false);
//
//            if (userIdText == null) {
//                log.error("Password reset reply missing userId: {}", payload);
//                return;
//            }
//
//            if (success) {
//                log.info("Password reset for user [{}] completed successfully", userIdText);
//            } else {
//                String error = payload.path("error").asText("Unknown error");
//                log.error("Password reset for user [{}] failed: {}", userIdText, error);
//            }
//
//        } catch (Exception e) {
//            log.error("Failed to process PASSWORD_RESET_RESPONSE: {}", payload, e);
//        }
//    }
}
