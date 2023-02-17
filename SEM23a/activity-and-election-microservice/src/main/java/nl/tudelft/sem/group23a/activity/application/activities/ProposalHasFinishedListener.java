package nl.tudelft.sem.group23a.activity.application.activities;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.domain.activities.events.ProposalHasFinishedEvent;
import nl.tudelft.sem.group23a.activity.services.ProposalProcedureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener for finishing proposals.
 */
@Component
@Profile("!test")
@RequiredArgsConstructor
public class ProposalHasFinishedListener {

    private static final Logger logger = LoggerFactory.getLogger(ProposalHasFinishedListener.class);

    private final transient ProposalProcedureService proposalProcedureService;

    @EventListener
    public void onProposalHasFinishedEvent(ProposalHasFinishedEvent event) {
        logger.info(String.format("Proposal with id = %d has finished", event.getActivityId()));
        proposalProcedureService.handleEndOfProposal(event.getActivityId());
    }
}
