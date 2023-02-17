package nl.tudelft.sem.group23a.authentication.application.user;

import nl.tudelft.sem.group23a.authentication.domain.events.UserWasCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserWasCreatedListener {
    @EventListener
    public void onUserCreation(UserWasCreatedEvent event) {
        System.out.println("Created new account -> " + event.getUsername().toString());
    }
}
