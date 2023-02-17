package nl.tudelft.sem.group23a.notifications.services.notifications.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Representation of a notification.
 */
@Data
@AllArgsConstructor
public class Notification {

    /**
     * The subject of the notification.
     */
    private final String subject;

    /**
     * The content of the notification.
     */
    private final String content;
}
