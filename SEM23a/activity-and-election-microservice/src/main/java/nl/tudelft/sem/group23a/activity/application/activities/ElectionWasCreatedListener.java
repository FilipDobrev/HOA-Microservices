package nl.tudelft.sem.group23a.activity.application.activities;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.domain.activities.events.ElectionWasCreatedEvent;
import nl.tudelft.sem.group23a.activity.services.ElectionProcedureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener for election creation.
 */
@Component
@Profile("!test")
@RequiredArgsConstructor
public class ElectionWasCreatedListener {

    private static final Logger logger = LoggerFactory.getLogger(ElectionWasCreatedListener.class);

    private final transient ElectionProcedureService electionProcedureService;

    /**
     * Starts the final election procedure.
     *
     * @param event the application creation event
     */
    @EventListener
    public void onApplicationCreation(ElectionWasCreatedEvent event) {
        logger.info(String.format("Election activity for hoa with id = %d has been created", event.getHoaId()));
        this.electionProcedureService.handleElectionProcedure(event.getHoaId());
    }
}