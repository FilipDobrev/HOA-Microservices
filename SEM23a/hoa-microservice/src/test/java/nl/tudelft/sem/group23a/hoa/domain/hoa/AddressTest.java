package nl.tudelft.sem.group23a.hoa.domain.hoa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void invalidNameOfCountry() {
        assertThatThrownBy(() -> new Address("BUlgaria", "Varna"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid name of country");
    }

    @Test
    public void invalidNameOfCity() {
        // Name of the city starts with spaces
        assertThatThrownBy(() -> new Address("Bulgaria", "  Varna"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid name of city");

        // Name of city ends with a hyphen
        assertThatThrownBy(() -> new Address("Bulgaria", "Varna-"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid name of city");
    }
}
