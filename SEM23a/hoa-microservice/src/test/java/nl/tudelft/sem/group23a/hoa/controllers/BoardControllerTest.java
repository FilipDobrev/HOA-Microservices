package nl.tudelft.sem.group23a.hoa.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import nl.tudelft.sem.group23a.hoa.domain.hoa.HoaBoardService;
import nl.tudelft.sem.group23a.hoa.models.BoardMembersModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BoardControllerTest {

    private HoaBoardService hoaBoardService;
    private BoardController boardController;

    @BeforeEach
    public void setup() {
        this.hoaBoardService = mock(HoaBoardService.class);
        this.boardController = new BoardController(this.hoaBoardService);
    }

    @Test
    void setsBoardMembersTest() {
        // arrange
        long hoaId = 1;
        List<String> board = List.of("user1", "user2");

        // act
        ResponseEntity<?> response = this.boardController.setsBoardMembers(hoaId, new BoardMembersModel(board));

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(this.hoaBoardService, times(1)).setBoard(hoaId, board);
        verify(this.hoaBoardService, times(1)).setBoard(any(), any());
    }

    @Test
    void getsBoardMembersTest() {
        // arrange
        long hoaId = 1;
        List<String> board = List.of("user1", "user2");
        when(this.hoaBoardService.getBoard(hoaId)).thenReturn(board);

        // act
        ResponseEntity<BoardMembersModel> response = this.boardController.getsBoardMembers(hoaId);

        // assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBoardMembers()).isEqualTo(board);
    }
}
