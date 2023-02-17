package nl.tudelft.sem.group23a.hoa.domain.hoa;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A DDD value object representing an address in the domain.
 */
@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Address {

    private String country;
    private String city;

    public Address(String country, String city) {
        this.country = validateCountry(country);
        this.city = validateCity(city);
    }

    /**
     * Validates a country name.
     * It enforces
     * 		- every part of the name to start with a capital letter
     * 		- every letter except for the first one should be lowercase
     * 		- no other characters except for letters are allowed
     * Drawback!! Does not allow stuff like 'United States of America'
     *
     * @param country the country to validate
     * @return the validated country
     */
    private String validateCountry(String country) {
        if (!country.matches("^[a-z]?[A-Z][a-z]+( [A-Z][a-z]+)*$")) {
            throw new IllegalArgumentException("Invalid name of country");
        }
        return country;
    }

    /**
     * Validates a city name.
     * It enforces
     * 		- The city to start with a capital letter
     * 		- The city can contain only letters at the front and at the end
     * 		- Allows hyphens in the middle of the name of the city
     *
     * @param city the city for validation
     * @return the name of the city
     */
    private String validateCity(String city) {
        if (!city.matches("^[A-Z][a-zA-Z]*(?:[\\s-][a-zA-Z]+)*$")) {
            throw new IllegalArgumentException("Invalid name of city");
        }
        return city;
    }
}
