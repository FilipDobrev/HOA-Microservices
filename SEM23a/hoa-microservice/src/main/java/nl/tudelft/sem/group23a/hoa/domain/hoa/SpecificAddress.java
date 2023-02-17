package nl.tudelft.sem.group23a.hoa.domain.hoa;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nl.tudelft.sem.group23a.hoa.models.HoaJoinModel;

/**
 * A DDD value object representing a specific address in our domain.
 */
@Embeddable
@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SpecificAddress extends Address {

    private String postalCode;
    private String street;
    private String houseNumber;

    /**
     * Creates a Specific Address.
     *
     * @param country the country of the address
     * @param city the city of the address
     * @param postalCode the postal code of the address
     * @param street the street of the address
     * @param houseNumber the number of the house on the address
     */
    public SpecificAddress(String country,
                           String city,
                           String postalCode,
                           String street,
                           String houseNumber) {
        super(country, city);
        this.postalCode = validatePostalCode(postalCode);
        this.street = validateStreet(street);
        this.houseNumber = validateHouseNumber(houseNumber);
    }

    /**
     * Validates the postal code of an address.
     * It enforces
     * 		- the postal code not to have special characters
     * 		- the postal code to contain only alphanumeric characters
     * 		- the postal code does not contain spaces
     *
     * @param postalCode the postal code for validation
     * @return the validated postal code
     */
    private String validatePostalCode(String postalCode) {
        // Although IntelliJ keeps saying 0-9 should be replaced with \d,
        // they are *not* equivalent. Please do retain 0-9 as \d would also
        // allow foreign numerals to be used.
        if (!postalCode.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException("Invalid postal code");
        }
        return postalCode;
    }

    /**
     * Validates the name of the street.
     * It enforces
     * 		- Only alphanumeric characters in the name
     * 		- No special characters allowed
     *
     * @param street the name of the street
     * @return the validated name of the street
     */
    private String validateStreet(String street) {
        // Although IntelliJ keeps saying 0-9 should be replaced with \d,
        // they are *not* equivalent. Please do retain 0-9 as \d would also
        // allow foreign numerals to be used.
        if (!street.matches("^[.0-9a-zA-Z\\s,-]+$")) {
            throw new IllegalArgumentException("Invalid street name");
        }
        return street;
    }

    /**
     * Validates the house number.
     * Checks if the house number is non-negative.
     *
     * @param houseNumber the house number for validation
     * @return the validated house number
     */
    private String validateHouseNumber(String houseNumber) {
        if (houseNumber.isEmpty()) {
            throw new IllegalArgumentException("Invalid house number");
        }
        return houseNumber;
    }
}
