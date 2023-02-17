package nl.tudelft.sem.group23a.activity.models;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

public class ProposalVotingRequestModelTest {

    @Test
    void serializableTest() {
        assertThat(new ProposalVotingRequestModel()).isNotNull();
    }
}