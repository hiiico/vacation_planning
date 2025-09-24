package app.event.payload;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationPreferenceResponseKafka {

    private UUID id;
    private UUID userId;
    private NotificationTypeRequest type;
    private boolean enabled;
    private String contactInfo;

    @Builder.Default
    private boolean success = true;

    private String error;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
