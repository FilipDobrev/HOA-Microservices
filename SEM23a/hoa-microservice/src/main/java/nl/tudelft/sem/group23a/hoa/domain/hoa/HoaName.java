package nl.tudelft.sem.group23a.hoa.domain.hoa;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A DDD Object representing the hoa name.
 */
@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class HoaName {

    private String hoaNameValue;

    /**
     * Validates and sets the HOA name.
     *
     * @param hoaName the HOA's name.
     */
    public HoaName(String hoaName) {
        this.hoaNameValue = this.validateHoaName(hoaName);
    }

    /**
     * Checks if the hoa's name starts with a letter, ends in an alphanumeric character,
     * and contains only alphanumeric characters, _, and -.
     *
     * @param hoaName The string for which to check this, the hoa's name.
     * @return a boolean indicating whether it matched.
     */
    private String validateHoaName(String hoaName) {
        // Although IntelliJ keeps saying 0-9 should be replaced with \d,
        // they are *not* equivalent. Please do retain 0-9 as \d would also
        // allow foreign numerals to be used.
        if (!hoaName.matches("^[a-zA-Z]([a-zA-Z0-9_\\- ]*[a-zA-Z0-9])?$")) {
            throw new IllegalArgumentException("Invalid HOA name");
        }
        return hoaName;
    }
}
