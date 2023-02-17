package nl.tudelft.sem.group23a.activity.domain.voting;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nl.tudelft.sem.group23a.activity.domain.activities.Vote;

public class MostVotesVotingStrategy implements VotingStrategy {

    public static final int BOARD_MEMBERS = 5;

    @Override
    public List<String> getResult(Set<Vote> votes) {
        Map<String, Integer> map = VotingStrategy.aggregateResults(votes);

        return map
                .entrySet()
                .stream()
                .sorted((x, y) -> -Integer.compare(x.getValue(), y.getValue()))
                .limit(BOARD_MEMBERS)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
