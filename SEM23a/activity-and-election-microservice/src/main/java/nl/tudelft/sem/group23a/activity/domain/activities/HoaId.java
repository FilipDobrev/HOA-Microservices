package nl.tudelft.sem.group23a.activity.domain.activities;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoaId {

    private long hoaIdValue;

    public HoaId(long hoaIdValue) {
        this.hoaIdValue = validateHoaId(hoaIdValue);
    }

    /**
     * Validates the Hoa ID.
     * It enforces
     *     - HOA ID must be a non-negative long integer
     *
     * @param hoaId ID of the HOA
     * @return same ID if the validation was successful
     */
    private long validateHoaId(long hoaId) {
        if (hoaId < 0) {
            throw new IllegalArgumentException("Invalid HoaID");
        }
        return hoaId;
    }
}
