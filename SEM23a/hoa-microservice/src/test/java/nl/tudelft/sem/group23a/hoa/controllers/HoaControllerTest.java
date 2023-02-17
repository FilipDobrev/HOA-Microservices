package nl.tudelft.sem.group23a.hoa.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.group23a.commons.ActivityResponseModel;
import nl.tudelft.sem.group23a.hoa.authentication.AuthManager;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaMembershipService;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaService;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.ActivityService;
import nl.tudelft.sem.group23a.hoa.models.HoaCreationModel;
import nl.tudelft.sem.group23a.hoa.models.HoaIdModel;
import nl.tudelft.sem.group23a.hoa.models.HoaJoinModel;
import nl.tudelft.sem.group23a.hoa.models.PeopleInHoaResponseModel;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class HoaControllerTest {

    private HoaMembershipService hoaMembershipService;
    private HoaService hoaService;
    private ActivityService activityService;
    private AuthManager authManager;
    private HttpServletRequest request;
    private HoaController hoaController;

    @BeforeEach
    public void setup() {
        this.hoaMembershipService = mock(HoaMembershipService.class);
        this.hoaService = mock(HoaService.class);
        this.authManager = mock(AuthManager.class);
        this.activityService = mock(ActivityService.class);
        this.request = mock(HttpServletRequest.class);
        this.hoaController = new HoaController(this.hoaMembershipService,
                this.hoaService,
                this.activityService,
                this.authManager,
                this.request);
    }

    @Test
    void createHoa() {
        HoaCreationModel hoaCreationModel = new HoaCreationModel("name", "country", "city");
        when(this.hoaService.createHoa(any(HoaCreationModel.class))).thenReturn(1L);

        ResponseEntity<HoaIdModel> result = this.hoaController.createHoa(hoaCreationModel);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(result.getBody()).getId()).isEqualTo(1L);
    }

    @Test
    void joinHoaNotFound() {
        when(this.authManager.getId()).thenReturn("username");
        when(this.hoaService.joinHoa(any(Long.class), any(HoaJoinModel.class), anyString()))
                .thenReturn(HoaService.HoaJoinOutcome.HOA_DOES_NOT_EXIST);

        HoaJoinModel joinDto = mock(HoaJoinModel.class);

        ResponseEntity<?> result = this.hoaController.joinHoa(1L, joinDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void joinHoaAccepted() {
        when(this.authManager.getId()).thenReturn("username");
        when(this.hoaService.joinHoa(anyLong(), any(HoaJoinModel.class), anyString()))
                .thenReturn(HoaService.HoaJoinOutcome.ACCEPTED_TO_HOA);

        HoaJoinModel joinDto = mock(HoaJoinModel.class);

        ResponseEntity<?> result = this.hoaController.joinHoa(1L, joinDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void joinHoaWrongLocality() {
        when(this.authManager.getId()).thenReturn("username");
        when(this.hoaService.joinHoa(anyLong(), any(HoaJoinModel.class), anyString()))
                .thenReturn(HoaService.HoaJoinOutcome.WRONG_LOCALITY_FOR_HOA);

        HoaJoinModel joinDto = mock(HoaJoinModel.class);

        ResponseEntity<?> result = this.hoaController.joinHoa(1L, joinDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void joinHoaAlreadyMember() {
        when(this.authManager.getId()).thenReturn("username");
        when(this.hoaService.joinHoa(anyLong(), any(HoaJoinModel.class), anyString()))
                .thenReturn(HoaService.HoaJoinOutcome.ALREADY_IN_HOA);

        HoaJoinModel joinDto = mock(HoaJoinModel.class);

        ResponseEntity<?> result = this.hoaController.joinHoa(1L, joinDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void joinHoaInternalServerError() {
        HoaJoinModel joinDto = mock(HoaJoinModel.class);

        ResponseEntity<?> result = this.hoaController.joinHoa(1L, joinDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void leaveHoaNotPartOf() {
        when(this.hoaService.leaveHoa(any(Long.class), anyString())).thenReturn(false);
        when(this.authManager.getId()).thenReturn("someUserId");

        ResponseEntity<?> result = this.hoaController.leaveHoa(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void leaveHoaSuccessfully() {
        when(this.hoaService.leaveHoa(any(Long.class), anyString())).thenReturn(true);
        when(this.authManager.getId()).thenReturn("someUserId");

        ResponseEntity<?> result = this.hoaController.leaveHoa(1L);

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void validIdOfHoa() {
        List<String> memberIds = List.of("1", "4", "7");

        when(this.hoaService.memberIds(1L)).thenReturn(memberIds);

        ResponseEntity<PeopleInHoaResponseModel> expected = ResponseEntity.ok(new PeopleInHoaResponseModel(memberIds));

        assertThat(this.hoaController.allMembers(1L)).isEqualTo(expected);
    }

    @Test
    void getNoticeBoardReturnsBadRequestForNonAllowedBoardAccess() {
        long hoaId = 1;
        String userId = "someUserId";
        when(this.hoaMembershipService.allowedToGetNoticeboard(hoaId, userId)).thenReturn(false);
        when(this.authManager.getId()).thenReturn(userId);

        ResponseEntity<?> result = this.hoaController.getNoticeBoard(hoaId);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getNoticeBoardReturnsActivities() {
        long hoaId = 1;
        String userId = "someUserId";
        String bearer = "Bearer aaa";
        when(this.hoaMembershipService.allowedToGetNoticeboard(hoaId, userId)).thenReturn(true);
        when(this.request.getHeader("Authorization")).thenReturn(bearer);
        when(this.authManager.getId()).thenReturn(userId);
        when(this.activityService.getActivities(hoaId, "aaa", ActivityService.Time.FUTURE))
                .thenReturn(new ActivityResponseModel[] {  new ActivityResponseModel()  });

        ResponseEntity<?> result = this.hoaController.getNoticeBoard(hoaId);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isInstanceOf(ActivityResponseModel[].class);
        assertThat(result.getBody()).asInstanceOf(InstanceOfAssertFactories.ARRAY).hasSize(1);
    }

    @Test
    void getHoaHistoryReturnsBadRequestForNonAllowedAccess() {
        long hoaId = 1;
        String userId = "someUserId";
        when(this.hoaMembershipService.allowedToGetNoticeboard(hoaId, userId)).thenReturn(false);
        when(this.authManager.getId()).thenReturn(userId);

        ResponseEntity<?> result = this.hoaController.getHoaHistory(hoaId);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getHoaHistoryReturnsActivities() {
        long hoaId = 1;
        String userId = "someUserId";
        String bearer = "Bearer aaa";
        when(this.hoaMembershipService.allowedToGetNoticeboard(hoaId, userId)).thenReturn(true);
        when(this.request.getHeader("Authorization")).thenReturn(bearer);
        when(this.authManager.getId()).thenReturn(userId);
        when(this.activityService.getActivities(hoaId, "aaa", ActivityService.Time.PAST))
                .thenReturn(new ActivityResponseModel[] {  new ActivityResponseModel()  });

        ResponseEntity<?> result = this.hoaController.getHoaHistory(hoaId);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isInstanceOf(ActivityResponseModel[].class);
        assertThat(result.getBody()).asInstanceOf(InstanceOfAssertFactories.ARRAY).hasSize(1);
    }
}