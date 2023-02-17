package nl.tudelft.sem.group23a.activity.domain.voting;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.tudelft.sem.group23a.activity.domain.activities.Vote;

public class ApplicationVotingStrategy implements VotingStrategy {

    @Override
    public List<String> getResult(Set<Vote> votes) {
        return votes
                .stream()
                .filter(x -> x.getChoice().equals("Yes"))
                .map(Vote::getUsername)
                .collect(Collectors.toList());
    }
}
