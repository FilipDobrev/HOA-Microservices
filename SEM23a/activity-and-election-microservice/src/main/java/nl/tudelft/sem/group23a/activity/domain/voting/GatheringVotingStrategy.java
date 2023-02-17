package nl.tudelft.sem.group23a.activity.domain.voting;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.tudelft.sem.group23a.activity.domain.activities.Vote;

public class GatheringVotingStrategy implements VotingStrategy {

    @Override
    public List<String> getResult(Set<Vote> votes) {

        return votes.stream()
                .collect(Collectors.groupingByConcurrent(Vote::getChoice, Collectors.counting()))
                .entrySet()
                .stream()
                .map(e -> String.format("%s: %d", e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}