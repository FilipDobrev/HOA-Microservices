package nl.tudelft.sem.group23a.activity.domain.voting;

import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.tudelft.sem.group23a.activity.domain.activities.Vote;

public class ProposalVotingStrategy implements VotingStrategy {

    public static final String YES = "Yes";
    public static final String NO = "No";

    @Override
    public List<String> getResult(Set<Vote> votes) {
        Map<String, Integer> map = VotingStrategy.aggregateResults(votes);

        return map.getOrDefault(YES, 0) > map.getOrDefault(NO, 0) ? List.of(YES) : List.of(NO);
    }
}
