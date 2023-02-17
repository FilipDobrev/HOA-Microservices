package nl.tudelft.sem.group23a.hoa.application.hoa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.hoa.domain.hoa.events.HoaWasCreatedEvent;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.ElectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
public class HoaWasCreatedListenerTest {

    private ElectionService electionService;

    private HoaWasCreatedListener hoaWasCreatedListener;

    @BeforeEach
    public void setup() {
        electionService = mock(ElectionService.class);
        hoaWasCreatedListener = new HoaWasCreatedListener(electionService);
    }

    @Test
    void eventHandlerMakesCallToStartElection() {
        // arrange
        long hoaId = 2;
        when(electionService.startElectionProcess(hoaId)).thenReturn(Result.successful());

        // act
        hoaWasCreatedListener.onHoaCreation(new HoaWasCreatedEvent(hoaId));

        // assert
        verify(electionService, times(1)).startElectionProcess(hoaId);
    }

    @Test
    public void eventHandlerLogsErrors(CapturedOutput output) {
        // arrange
        long hoaId = 2;
        String[] errors = new String[] {"error1", "error2"};
        when(electionService.startElectionProcess(hoaId)).thenReturn(Result.unsuccessful(errors));

        // act
        hoaWasCreatedListener.onHoaCreation(new HoaWasCreatedEvent(hoaId));

        // assert
        verify(electionService, times(1)).startElectionProcess(hoaId);
        assertThat(output.getOut()).contains(errors[0]);
        assertThat(output.getOut()).contains(errors[1]);
    }
}
