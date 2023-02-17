package nl.tudelft.sem.group23a.activity.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.group23a.activity.domain.activities.ActivityRepository;
import nl.tudelft.sem.group23a.activity.domain.activities.Application;
import nl.tudelft.sem.group23a.activity.domain.activities.ApplicationRepository;
import nl.tudelft.sem.group23a.activity.domain.activities.Description;
import nl.tudelft.sem.group23a.activity.domain.activities.Election;
import nl.tudelft.sem.group23a.activity.domain.activities.ElectionRepository;
import nl.tudelft.sem.group23a.activity.domain.activities.HoaId;
import nl.tudelft.sem.group23a.commons.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElectionProcedureServiceTest {

    private ElectionRepository electionRepository;
    private ActivityRepository activityRepository;
    private ApplicationRepository applicationRepository;

    private ElectionProcedureService electionService;

    @BeforeEach
    void setUp() {
        this.electionRepository = mock(ElectionRepository.class);
        this.activityRepository = mock(ActivityRepository.class);
        this.applicationRepository = mock(ApplicationRepository.class);
        this.electionService = new ElectionProcedureService(applicationRepository, electionRepository,
                activityRepository, null);
    }

    @Test
    void applyForElection() {
        // Arrange
        Application application = new Application(new HoaId(2L), new Description("Apply here!"));

        when(applicationRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(1L))).thenReturn(Optional.empty());
        when(applicationRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(2L))).thenReturn(Optional.of(application));

        // Act
        Result resultUnsuccessful = electionService.applyForElection(1L, "Yes", "Me");
        Result resultSuccessful = electionService.applyForElection(2L, "No", "Me");
        ThrowingCallable resultThrows = () -> electionService.applyForElection(2L, "Invalid", "Me");

        // Assert
        assertThat(resultUnsuccessful.isSuccess()).isFalse();
        assertThat(resultSuccessful.isSuccess()).isEqualTo(true);
        assertThatThrownBy(resultThrows).isInstanceOf(IllegalArgumentException.class);
        verify(activityRepository, times(1)).save(any(Application.class));
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void voteForElection() {
        // Arrange
        Election election = new Election(new HoaId(2L), new Description("An election"),
                new HashSet<>(List.of("Me", "SomeoneElse")));

        when(electionRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(1L))).thenReturn(Optional.empty());
        when(electionRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(2L))).thenReturn(Optional.of(election));

        // Act
        Result resultUnsuccessful = electionService.voteForElection(1L, "SomeChoice", "Me");
        Result resultSuccessful = electionService.voteForElection(2L, "SomeoneElse", "Me");
        ThrowingCallable resultThrows = () -> electionService.voteForElection(2L, "NonExistentChoice", "Me");

        // Assert
        assertThat(resultUnsuccessful.isSuccess()).isFalse();
        assertThat(resultSuccessful.isSuccess()).isEqualTo(true);
        assertThatThrownBy(resultThrows).isInstanceOf(IllegalArgumentException.class);
        verify(activityRepository, times(1)).save(any(Election.class));
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void startElectionProcess() {
        electionService.startElectionProcess(1L);

        verify(activityRepository, times(1)).save(any(Application.class));
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void hasCurrentlyRunningElection() {
        // Arrange
        Calendar calendarBefore = new GregorianCalendar();
        calendarBefore.add(Calendar.DATE, -1);

        Calendar calendarAfter = new GregorianCalendar();
        calendarAfter.add(Calendar.DATE, 1);

        Election electionHoa1 = new Election(new HoaId(1), new Description("Some description"),
                new HashSet<>(List.of("One", "Two")));
        Election electionHoa4 = new Election(new HoaId(4), new Description("Another description"),
                new HashSet<>(List.of("Option", "More option")));
        Election electionHoa6 = mock(Election.class);
        Election electionHoa7 = mock(Election.class);

        when(electionHoa6.getTimestamp()).thenReturn(calendarBefore);
        when(electionHoa7.getTimestamp()).thenReturn(calendarAfter);

        when(electionRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(1))).thenReturn(Optional.of(electionHoa1));
        when(electionRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(2))).thenReturn(Optional.empty());
        when(electionRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(3))).thenReturn(Optional.empty());
        when(electionRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(4))).thenReturn(Optional.of(electionHoa4));
        when(electionRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(5))).thenReturn(Optional.empty());
        when(electionRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(6))).thenReturn(Optional.of(electionHoa6));
        when(electionRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(7))).thenReturn(Optional.of(electionHoa7));

        Application applicationHoa2 = spy(Application.class);
        applicationHoa2.setHoaId(new HoaId(2));

        when(applicationHoa2.getTimestamp()).thenReturn(calendarBefore);

        Application applicationHoa4 = new Application(new HoaId(4), new Description("Application for board"));
        Application applicationHoa5 = new Application(new HoaId(5), new Description("Application for board"));

        when(applicationRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(1))).thenReturn(Optional.empty());
        when(applicationRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(2))).thenReturn(Optional.of(applicationHoa2));
        when(applicationRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(3))).thenReturn(Optional.empty());
        when(applicationRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(4))).thenReturn(Optional.of(applicationHoa4));
        when(applicationRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(5))).thenReturn(Optional.of(applicationHoa5));
        when(applicationRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(6))).thenReturn(Optional.of(applicationHoa2));
        when(applicationRepository.findFirstByHoaIdOrderByIdDesc(new HoaId(7))).thenReturn(Optional.of(applicationHoa2));

        // Act
        boolean result1 = electionService.hasCurrentlyRunningElection(1L);
        boolean result2 = electionService.hasCurrentlyRunningElection(2L);
        boolean result3 = electionService.hasCurrentlyRunningElection(3L);
        boolean result4 = electionService.hasCurrentlyRunningElection(4L);
        boolean result5 = electionService.hasCurrentlyRunningElection(5L);
        boolean result6 = electionService.hasCurrentlyRunningElection(6L);
        boolean result7 = electionService.hasCurrentlyRunningElection(7L);

        // Assert
        assertThat(result1).isFalse();
        assertThat(result2).isFalse();
        assertThat(result3).isFalse();
        assertThat(result4).isTrue();
        assertThat(result5).isTrue();
        assertThat(result6).isFalse();
        assertThat(result7).isTrue();
    }

    @Test
    void isCurrentlyRunningInElection() {
        when(electionRepository.existsByChoicesContaining("user")).thenReturn(true);

        boolean resultTrue = electionService.isCurrentlyRunningInElection("user");
        boolean resultFalse = electionService.isCurrentlyRunningInElection("another-user");

        assertThat(resultTrue).isTrue();
        assertThat(resultFalse).isFalse();
    }
}