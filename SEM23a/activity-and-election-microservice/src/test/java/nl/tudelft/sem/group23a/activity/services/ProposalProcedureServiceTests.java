package nl.tudelft.sem.group23a.activity.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.group23a.activity.domain.activities.ActivityRepository;
import nl.tudelft.sem.group23a.activity.domain.activities.Application;
import nl.tudelft.sem.group23a.activity.domain.activities.Description;
import nl.tudelft.sem.group23a.activity.domain.activities.Gathering;
import nl.tudelft.sem.group23a.activity.domain.activities.HoaId;
import nl.tudelft.sem.group23a.activity.domain.activities.Proposal;
import nl.tudelft.sem.group23a.activity.domain.activities.Vote;
import nl.tudelft.sem.group23a.activity.models.NotificationRequestModel;
import nl.tudelft.sem.group23a.activity.models.PeopleInHoaResponseModel;
import nl.tudelft.sem.group23a.commons.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ProposalProcedureServiceTests {

    private ActivityRepository activityRepository;
    private RestTemplate restTemplate;
    private ListAppender<ILoggingEvent> listAppender;
    private ProposalProcedureService proposalService;

    @Value("{urls.notificationsUrl}")
    private transient String notificationsUrl;

    @Value("{urls.hoaUrl}")
    private transient String hoaUrl;

    /**
     * The setup of the method.
     */
    @BeforeEach
    void setup() {
        this.activityRepository = mock(ActivityRepository.class);
        this.restTemplate = mock(RestTemplate.class);
        this.proposalService = new ProposalProcedureService(this.activityRepository, this.restTemplate);
        Logger logger = (Logger) LoggerFactory.getLogger(ProposalProcedureService.class);
        this.listAppender = new ListAppender<>();
        this.listAppender.start();
        logger.addAppender(this.listAppender);
        this.listAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
    }

    @Test
    void voteForNonExistentProposal() {
        // Arrange
        when(this.activityRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Result result = this.proposalService.voteForProposal(1L, "SomeChoice",
                "username", 2);

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).contains(ProposalProcedureService.Errors.PROPOSAL_NOT_FOUND);
        assertThat(result.getErrors().length).isEqualTo(1);
    }

    @Test
    void voteForProposalButInsteadForGathering() {
        // Arrange
        Gathering gathering = new Gathering();
        when(this.activityRepository.findById(anyLong())).thenReturn(Optional.of(gathering));

        // Act
        Result result = this.proposalService.voteForProposal(1L, "SomeChoice",
                "username", 2);

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).contains(ProposalProcedureService.Errors.PROPOSAL_NOT_FOUND);
        assertThat(result.getErrors().length).isEqualTo(1);
    }

    // Boundary test
    @Test
    void voteForProposalMinVotesAttainedOnBoundary() {
        // Arrange
        Proposal proposal = mock(Proposal.class);
        Vote vote = Vote.builder().username("username").build();

        when(proposal.getVotes()).thenReturn(new HashSet<>(List.of(vote)));
        when(this.activityRepository.findById(anyLong())).thenReturn(Optional.of(proposal));

        // Act
        Result result = this.proposalService.voteForProposal(1L, "SomeChoice",
                "username", 2);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        verify(proposal, times(1)).receivedEnoughVotes();
    }

    // Boundary test
    @Test
    void voteForProposalMinVotesNotAttainedByMinimalMargin() {
        // Arrange
        Proposal proposal = mock(Proposal.class);
        Vote vote = Vote.builder().username("username").build();

        when(proposal.getVotes()).thenReturn(new HashSet<>(List.of(vote)));
        when(this.activityRepository.findById(anyLong())).thenReturn(Optional.of(proposal));

        // Act
        Result result = this.proposalService.voteForProposal(1L, "SomeChoice",
                "username", 3);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        verify(proposal, never()).receivedEnoughVotes();
    }

    @Test
    void getIdsFromHoaNonExistentHoa() {
        String hoaUri = hoaUrl + "/hoa/members/1";
        when(restTemplate.exchange(hoaUri, HttpMethod.GET, null, PeopleInHoaResponseModel.class))
                .thenReturn(ResponseEntity.badRequest().build());

        Proposal proposal = new Proposal(new HoaId(1), new Description("desc"), 5);
        String[] result = proposalService.getIdsFromHoa(proposal);
        assertThat(result.length).isEqualTo(0);
        List<ILoggingEvent> logList = listAppender.list;
        assertThat(logList.get(0).getLevel())
                .isEqualTo(Level.ERROR);
        assertThat(logList.get(0).getMessage())
                .isEqualTo("The message to the hoa microservice encountered an error");
    }


    @Test
    void getIdsFromHoaValidResponse() {
        String hoaUri = hoaUrl + "/hoa/members/1";
        when(restTemplate.exchange(hoaUri, HttpMethod.GET, null, PeopleInHoaResponseModel.class))
                .thenReturn(ResponseEntity.ok().body(new PeopleInHoaResponseModel(List.of(1L, 2L))));

        Proposal proposal = new Proposal(new HoaId(1), new Description("desc"), 4);
        String[] result = proposalService.getIdsFromHoa(proposal);
        assertThat(result)
                .isEqualTo(new String[] {"1", "2"});
        List<ILoggingEvent> logList = listAppender.list;
        assertThat(logList)
                .isEmpty();
    }

    @Test
    void handleEndOfProposalRepoHasNoSuchActivity() {
        when(activityRepository.findById(Long.valueOf(1)))
                .thenReturn(Optional.empty());

        proposalService.handleEndOfProposal(1L);

        List<ILoggingEvent> logList = listAppender.list;
        assertThat(logList.get(0).getLevel())
                .isEqualTo(Level.ERROR);
        assertThat(logList.get(0).getMessage())
                .isEqualTo("The activity is not present in the database");
    }

    @Test
    void handleEndOfProposalActivityIsNotProposal() {
        Application application = new Application(new HoaId(1), new Description("desc"));
        when(activityRepository.findById(Long.valueOf(1)))
                .thenReturn(Optional.of(application));

        proposalService.handleEndOfProposal(1L);

        List<ILoggingEvent> logList = listAppender.list;
        assertThat(logList.get(0).getLevel())
                .isEqualTo(Level.ERROR);
        assertThat(logList.get(0).getMessage())
                .isEqualTo("The activity is not instance of proposal");
    }

    @Test
    void handleEndOfProposalProposalNotAccepted() {
        Proposal proposal = new Proposal(1, new HoaId(1), new Description("desc"), 3);
        proposal.vote("Yes", "Dimitar");
        proposal.vote("No", "Filip");
        proposal.vote("No", "Olek");

        when(activityRepository.findById(Long.valueOf(1)))
                .thenReturn(Optional.of(proposal));

        proposalService.handleEndOfProposal(1L);

        List<ILoggingEvent> logList = listAppender.list;
        assertThat(logList.get(0).getLevel())
                .isEqualTo(Level.INFO);
        assertThat(logList.get(0).getMessage())
                .isEqualTo("The proposal 1 has not been accepted");
    }

    @Test
    void handleEndOfProposalFailedNotifications() {
        Proposal proposal = new Proposal(1, new HoaId(1), new Description("Some description"), 2);
        proposal.vote("Yes", "Dimitar");
        proposal.vote("Yes", "Filip");
        proposal.vote("No", "Olek");

        String hoaUri = hoaUrl + "/hoa/members/1";
        when(restTemplate.exchange(hoaUri, HttpMethod.GET, null, PeopleInHoaResponseModel.class))
                .thenReturn(ResponseEntity.ok().body(new PeopleInHoaResponseModel(List.of(1L, 2L, 3L, 4L))));

        NotificationRequestModel request = new NotificationRequestModel(
                new String[] {"1", "2", "3", "4"},
                "Proposal accepted",
                "Some description");
        String notificationsUri = notificationsUrl + "/notifications/send";
        HttpHeaders headers = new HttpHeaders();
        when(restTemplate.exchange(notificationsUri,
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                Void.class))
                .thenReturn(ResponseEntity.badRequest().build());

        when(activityRepository.findById(Long.valueOf(1)))
                .thenReturn(Optional.of(proposal));

        proposalService.handleEndOfProposal(1L);

        List<ILoggingEvent> logList = listAppender.list;
        assertThat(logList.get(0).getLevel())
                .isEqualTo(Level.ERROR);
        assertThat(logList.get(0).getMessage())
                .isEqualTo("The notifications were not send successfully");
    }

    @Test
    void handleEndOfProposalNotificationsSentSuccessfully() {
        Proposal proposal = new Proposal(1, new HoaId(1), new Description("Some description"), 1);
        proposal.vote("Yes", "Dimitar");
        proposal.vote("Yes", "Filip");
        proposal.vote("No", "Olek");

        String hoaUri = hoaUrl + "/hoa/members/1";
        when(restTemplate.exchange(hoaUri, HttpMethod.GET, null, PeopleInHoaResponseModel.class))
                .thenReturn(ResponseEntity.ok().body(new PeopleInHoaResponseModel(List.of(1L, 2L, 3L, 4L))));

        NotificationRequestModel request = new NotificationRequestModel(
                new String[] {"1", "2", "3", "4"},
                "Proposal accepted",
                "Some description");
        String notificationsUri = notificationsUrl + "/notifications/send";
        HttpHeaders headers = new HttpHeaders();
        when(restTemplate.exchange(notificationsUri,
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                Void.class))
                .thenReturn(ResponseEntity.ok().build());

        when(activityRepository.findById(Long.valueOf(1)))
                .thenReturn(Optional.of(proposal));

        proposalService.handleEndOfProposal(1L);

        List<ILoggingEvent> logList = listAppender.list;
        assertThat(logList.get(0).getLevel())
                .isEqualTo(Level.INFO);
        assertThat(logList.get(0).getMessage())
                .isEqualTo("Notifications sent successfully for proposal 1");
    }

    @Test
    void handleEndOfProposalNoUsers() {
        Proposal proposal = new Proposal(1, new HoaId(1), new Description("Some description"), 1);
        proposal.vote("Yes", "Dimitar");
        proposal.vote("Yes", "Filip");
        proposal.vote("No", "Olek");

        String hoaUri = hoaUrl + "/hoa/members/1";
        when(this.restTemplate.exchange(hoaUri, HttpMethod.GET, null, PeopleInHoaResponseModel.class))
                .thenReturn(ResponseEntity.ok().build());
        when(activityRepository.findById(Long.valueOf(1)))
                .thenReturn(Optional.of(proposal));

        this.proposalService.handleEndOfProposal(1L);

        verify(this.restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), eq(null),
                eq(PeopleInHoaResponseModel.class));
        verifyNoMoreInteractions(this.restTemplate);
    }
}
