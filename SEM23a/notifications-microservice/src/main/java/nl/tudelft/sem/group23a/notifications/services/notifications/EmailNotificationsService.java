package nl.tudelft.sem.group23a.notifications.services.notifications;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.notifications.infrastructure.integrations.users.UserService;
import nl.tudelft.sem.group23a.notifications.infrastructure.services.EmailService;
import nl.tudelft.sem.group23a.notifications.services.notifications.models.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service that handles email notifications.
 */
@Service
public class EmailNotificationsService implements NotificationsService {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationsService.class);

    private final transient EmailService emailService;
    private final transient UserService userService;

    @Value("${email.from}")
    private transient String fromEmail;

    /**
     * EmailNotificationsService construct for dependency injection.
     *
     * @param emailService emailService.
     * @param userService userService.
     */
    public EmailNotificationsService(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    /**
     * Sends an email notification.
     *
     * @param identifier user identifier.
     * @param notification notification data.
     * @return the result of the operation.
     */
    @Override
    public Result sendNotification(String identifier, Notification notification) {
        logger.info("Sending email notification from {} to {}.", fromEmail, identifier);

        var userEmailResult = userService.getEmail(identifier);

        if (!userEmailResult.isSuccess()) {
            return userEmailResult;
        }

        var email = userEmailResult.getData();

        return emailService.sendEmail(fromEmail, email, notification.getSubject(), notification.getContent());
    }

    /**
     * Sends an email notification to multiple receivers.
     *
     * @param identifiers user identifiers.
     * @param notification notification data.
     * @return the result of the operation.
     */
    @Override
    public Result sendNotification(String[] identifiers, Notification notification) {
        var errors = new ArrayList<String>();
        for (var identifier : identifiers) {
            var notificationResult = sendNotification(identifier, notification);

            if (!notificationResult.isSuccess()) {
                errors.addAll(List.of(notificationResult.getErrors()));
            }
        }

        if (errors.size() > 0) {
            return Result.unsuccessful(errors.toArray(new String[0]));
        }

        return Result.successful();
    }
}
