package nl.tudelft.sem.group23a.activity.domain.activities;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DDD value object representing the description in the domain.
 */
@Data
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Description {

    private String descriptionValue;

    public Description(String description) {
        this.descriptionValue = validateDescription(description);
    }

    /**
     * Validates the description.
     * It enforces.
     *     - Description must have at least one alphabet symbol on the first line
     *
     * @param description String to be validated for input
     * @return same String if the validation was successful
     */
    private String validateDescription(String description) {
        if (!description.matches("^[a-zA-Z]+(.|\\s)*$")) {
            throw new IllegalArgumentException("Invalid description");
        }
        return description;
    }
}
