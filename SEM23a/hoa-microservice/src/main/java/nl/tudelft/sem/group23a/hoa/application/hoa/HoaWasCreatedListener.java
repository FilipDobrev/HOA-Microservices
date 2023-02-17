package nl.tudelft.sem.group23a.hoa.application.hoa;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.hoa.domain.hoa.events.HoaWasCreatedEvent;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.ElectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener for hoa creation.
 */
@Component
@Profile("!test")
@RequiredArgsConstructor
public class HoaWasCreatedListener {

    private static final Logger logger = LoggerFactory.getLogger(HoaWasCreatedListener.class);

    private final transient ElectionService electionService;

    /**
     * Sends a request to start the election procedure once a hoa is created.
     *
     * @param event the hoa creation event
     */
    @EventListener
    public void onHoaCreation(HoaWasCreatedEvent event) {
        logger.info(String.format("Hoa with id = %d has been created", event.getHoaId()));
        Result result = this.electionService.startElectionProcess(event.getHoaId());

        if (!result.isSuccess()) {
            logger.error(String.join(", ", result.getErrors()));
        }
    }
}