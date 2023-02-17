package nl.tudelft.sem.group23a.hoa.domain.hoa;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

public class SpecificAddressTest {

    @Test
    public void invalidPostalCode() {
        ThrowableAssert.ThrowingCallable action = () ->
                new SpecificAddress("Bulgaria", "Varna", "9000$", "20-ta", "9");
        assertThatThrownBy(action)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid postal code");
    }

    @Test
    public void invalidStreet() {
        ThrowableAssert.ThrowingCallable action = () ->
                new SpecificAddress("Bulgaria", "Varna", "9000", "20-ta$", "9");
        assertThatThrownBy(action)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid street name");
    }

    @Test
    public void invalidHouseNumber() {
        ThrowableAssert.ThrowingCallable action = () ->
                new SpecificAddress("Bulgaria", "Varna", "9000", "20-ta", "");
        assertThatThrownBy(action)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid house number");
    }
}
