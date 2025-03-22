package app.notification.employee.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationPreference {

    private String type;
    private boolean enabled;
    private String contactInfo;
}
