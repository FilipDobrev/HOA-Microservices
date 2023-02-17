package nl.tudelft.sem.group23a.activity.domain.activities.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Even that's dispatched once an application has been created.
 */
@Getter
@AllArgsConstructor
public class ApplicationWasCreatedEvent {
    private Long hoaId;
}
