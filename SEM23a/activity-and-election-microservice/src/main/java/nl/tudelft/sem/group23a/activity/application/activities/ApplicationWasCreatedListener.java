package nl.tudelft.sem.group23a.activity.application.activities;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.domain.activities.events.ApplicationWasCreatedEvent;
import nl.tudelft.sem.group23a.activity.services.ElectionProcedureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener for application creation.
 */
@Component
@Profile("!test")
@RequiredArgsConstructor
public class ApplicationWasCreatedListener {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationWasCreatedListener.class);

    private final transient ElectionProcedureService electionProcedureService;

    /**
     * Starts the application election procedure.
     *
     * @param event the application creation event
     */
    @EventListener
    public void onApplicationCreation(ApplicationWasCreatedEvent event) {
        logger.info(String.format("Application activity for hoa with id = %d has been created", event.getHoaId()));
        this.electionProcedureService.handleApplicationProcedure(event.getHoaId());
    }
}