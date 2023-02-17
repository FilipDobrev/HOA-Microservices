package nl.tudelft.sem.group23a.authentication.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tudelft.sem.group23a.authentication.domain.user.Username;

@Getter
@AllArgsConstructor
public class UserWasCreatedEvent {
    private final Username username;
}
