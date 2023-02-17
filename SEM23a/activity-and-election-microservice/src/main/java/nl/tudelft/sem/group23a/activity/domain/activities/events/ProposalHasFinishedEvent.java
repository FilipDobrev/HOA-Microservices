package nl.tudelft.sem.group23a.activity.domain.activities.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProposalHasFinishedEvent {
    private Long activityId;
}
