package nl.tudelft.sem.group23a.activity.integrations.hoa;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.integrations.hoa.models.BoardMembersModel;
import nl.tudelft.sem.group23a.commons.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Integration service for hoa boards.
 */
@Service
@RequiredArgsConstructor
public class HoaBoardService {

    private final transient RestTemplate restTemplate;

    @Value("${urls.hoaUrl}")
    private transient String hoaUrl;

    /**
     * Sends a request to internal endpoint to update the board members of a hoa.
     *
     * @param hoaId the id of the hoa
     * @param boardMembers list of board members usernames
     * @return the operation result
     */
    public Result updateBoardMembers(long hoaId, List<String> boardMembers) {
        String uri = String.format("%s/hoa/%d/board", hoaUrl, hoaId);
        BoardMembersModel model = new BoardMembersModel(boardMembers);
        HttpEntity<BoardMembersModel> request = new HttpEntity<>(model);

        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return Result.unsuccessful("Unable to update board members");
        }

        return Result.successful();
    }
}
