package nl.tudelft.sem.group23a.activity.domain.voting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.tudelft.sem.group23a.activity.domain.activities.Vote;

public interface VotingStrategy {

    /**
     * Gets the results of the voting.
     *
     * @param votes current votes
     * @return the results
     */
    List<String> getResult(Set<Vote> votes);

    /**
     * Method to aggregate the votes and count them.
     *
     * @param votes the votes to be aggregated
     * @return a map with an option and number of votes
     */
    static Map<String, Integer> aggregateResults(Set<Vote> votes) {
        Map<String, Integer> map = new HashMap<>();

        for (Vote vote : votes) {
            if (map.containsKey(vote.getChoice())) {
                map.put(vote.getChoice(), map.get(vote.getChoice()) + 1);
            } else {
                map.put(vote.getChoice(), 1);
            }
        }
        return map;
    }
}
