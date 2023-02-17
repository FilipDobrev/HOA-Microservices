package nl.tudelft.sem.group23a.hoa.domain.hoa.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Even that's dispatched once a hoa has been created.
 */
@Getter
@AllArgsConstructor
public class HoaWasCreatedEvent {
    private Long hoaId;
}
