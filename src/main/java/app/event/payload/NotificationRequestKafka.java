package app.event.payload;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NotificationRequestKafka {

    private UUID userId;

    private String subject;

    private String body;
}
