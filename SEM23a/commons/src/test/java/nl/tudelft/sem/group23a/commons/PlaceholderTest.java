package nl.tudelft.sem.group23a.commons;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class PlaceholderTest {

    @Test
    public void placeholderTest() {
        assertThat(new Placeholder(true).isYes()).isTrue();
    }
}
