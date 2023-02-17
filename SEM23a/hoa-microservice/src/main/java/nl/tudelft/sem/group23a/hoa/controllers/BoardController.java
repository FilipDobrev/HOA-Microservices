package nl.tudelft.sem.group23a.hoa.controllers;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.hoa.domain.hoa.HoaBoardService;
import nl.tudelft.sem.group23a.hoa.models.BoardMembersModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for hoa elections.
 */
@RestController
@RequestMapping("hoa")
@RequiredArgsConstructor
public class BoardController {

    private final transient HoaBoardService hoaBoardService;

    /**
     * Internal endpoint for setting board members of a hoa.
     *
     * @param hoaId the id of the hoa
     * @param requestModel usernames of the future board members
     * @return http status code base on the result
     */
    @PutMapping("{hoaId}/board")
    public ResponseEntity<?> setsBoardMembers(@Valid @PathVariable Long hoaId,
                                              @Valid @RequestBody BoardMembersModel requestModel) {
        this.hoaBoardService.setBoard(hoaId, requestModel.getBoardMembers());
        return ResponseEntity.noContent().build();
    }

    /**
     * A user endpoint for listing board members of a hoa.
     *
     * @param hoaId the id of the hoa
     * @return the board members' usernames
     */
    @GetMapping("{hoaId}/board")
    public ResponseEntity<BoardMembersModel> getsBoardMembers(@Valid @PathVariable Long hoaId) {
        List<String> boardMembers = this.hoaBoardService.getBoard(hoaId);
        return ResponseEntity.ok(new BoardMembersModel(boardMembers));
    }
}