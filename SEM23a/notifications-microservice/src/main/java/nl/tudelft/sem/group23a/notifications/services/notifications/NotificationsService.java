package nl.tudelft.sem.group23a.notifications.services.notifications;

import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.notifications.services.notifications.models.Notification;

/**
 * Service that handles notifications.
 */
public interface NotificationsService {

    /**
     * Sends a notification.
     *
     * @param identifier user identifier.
     * @param notification notification data.
     * @return the result of the operation.
     */
    Result sendNotification(String identifier, Notification notification);

    /**
     * Sends a notification to multiple receivers.
     *
     * @param identifiers user identifiers.
     * @param notification notification data.
     * @return the result of the operation.
     */
    Result sendNotification(String[] identifiers, Notification notification);
}
