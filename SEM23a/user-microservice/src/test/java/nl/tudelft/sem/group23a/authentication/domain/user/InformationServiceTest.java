package nl.tudelft.sem.group23a.authentication.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nl.tudelft.sem.group23a.authentication.domain.repositories.UserRepository;
import nl.tudelft.sem.group23a.authentication.domain.services.InformationService;
import nl.tudelft.sem.group23a.commons.exceptions.NotAllNecessaryFieldsAddedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class InformationServiceTest {

    private UserRepository mockRepo;
    private InformationService service;

    @BeforeEach
    public void setup() {
        mockRepo = mock(UserRepository.class);
        service = new InformationService(mockRepo);
    }

    @Test
    public void noSuchUser() {
        // Arrange
        when(mockRepo.findById(1L)).thenReturn(Optional.empty());

        // Act
        Email expected = new Email("");
        assertThat(service.getEmailById(1L)).isEqualTo(expected);
    }

    @Test
    public void userFound() throws NotAllNecessaryFieldsAddedException {
        // Arrange
        when(mockRepo.findById(1L)).thenReturn(Optional.of(
                HoaUser.builder()
                        .email(new Email("varna@gmail.com"))
                        .password(new EncodedPassword("pass"))
                        .username(new Username("Mitko"))
                        .build()
        ));

        // Act
        Email expected = new Email("varna@gmail.com");
        assertThat(service.getEmailById(1L)).isEqualTo(expected);

    }
}
