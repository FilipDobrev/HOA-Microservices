package nl.tudelft.sem.group23a.hoa.domain.hoa;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A DDD value object representing a member ID in our domain.
 */
@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MemberId {

    private String memberIdString;

    /**
     * Validates and sets the member ID.
     *
     * @param memberId the member's ID.
     */
    public MemberId(String memberId) {
        this.memberIdString = this.validateMemberId(memberId);
    }

    /**
     * Checks if the member's ID starts and ends with an alphanumeric character,
     * and contains only alphanumeric characters, _, and -.
     *
     * @param memberId The string for which to check this, the member's ID.
     * @return a boolean indicating whether it matched.
     */
    private String validateMemberId(String memberId) {
        // Although IntelliJ keeps saying 0-9 should be replaced with \d,
        // they are *not* equivalent. Please do retain 0-9 as \d would also
        // allow foreign numerals to be used.
        if (!memberId.matches("^[a-zA-Z0-9]([a-zA-Z0-9_\\-]*[a-zA-Z0-9])?$")) {
            throw new IllegalArgumentException("Invalid HOA name");
        }
        return memberId;
    }
}
