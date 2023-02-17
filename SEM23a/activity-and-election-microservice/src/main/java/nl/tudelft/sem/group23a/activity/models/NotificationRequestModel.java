package nl.tudelft.sem.group23a.activity.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Request model for sending notifications.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationRequestModel {

    /**
     * The usernames of the notification receivers.
     */
    @NotNull
    @NotEmpty
    private String[] usernames;

    /**
     * The subject of the notification.
     */
    @NotNull
    @Length(min = 1, max = 64)
    private String subject;

    /**
     * The content of the notification.
     */
    @NotNull
    @Length(min = 1, max = 640)
    private String content;
}
