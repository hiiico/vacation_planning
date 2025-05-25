package app.event;

import app.event.payload.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserRegisterEventProducer {

    public final KafkaTemplate<String, UserRegisterEvent> kafkaTemplate;

    public UserRegisterEventProducer(KafkaTemplate<String, UserRegisterEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(UserRegisterEvent event) {

        kafkaTemplate.send("user-register-event.v1", event);
        log.info("Successfully published registered event for user %s"
                .formatted(event.getUserId()));
    }
}
