package nl.tudelft.sem.group23a.notifications.controllers;

import javax.validation.Valid;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.notifications.models.NotificationRequestModel;
import nl.tudelft.sem.group23a.notifications.services.notifications.NotificationsService;
import nl.tudelft.sem.group23a.notifications.services.notifications.models.Notification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for notifications.
 * <p>
 * This controller is for internal usage, and will be called to send notifications.
 * </p>
 */
@RestController
@RequestMapping("notifications")
public class NotificationsController {

    private final transient NotificationsService notificationsService;

    /**
     * NotificationsController constructor for dependency injection.
     *
     * @param notificationsService notificationsService.
     */
    public NotificationsController(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    /**
     * An endpoint for sending notifications to specific users.
     *
     * @param requestModel request body.
     * @return whether the operation was successful.
     */
    @PostMapping("send")
    public ResponseEntity<String[]> sendNotification(@Valid @RequestBody NotificationRequestModel requestModel) {
        Notification notification = new Notification(requestModel.getSubject(), requestModel.getContent());
        Result sendResult = notificationsService.sendNotification(requestModel.getUsernames(), notification);

        if (!sendResult.isSuccess()) {
            return ResponseEntity.badRequest()
                    .body(sendResult.getErrors());
        }

        return ResponseEntity.ok().build();
    }
}
