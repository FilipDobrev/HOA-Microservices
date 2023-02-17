package nl.tudelft.sem.group23a.activity.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group23a.commons.ActivityType;
import org.hibernate.validator.constraints.Length;

/**
 * Data transfer object for the creation of an Activity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityCreationRequestModel {

    @Min(value = 1, message = "provide valid hoa id")
    private long hoaId;

    @NotNull
    @Length(max = 640)
    @Pattern(regexp = "^[a-zA-Z]+(.|\\s)*$", message = "invalid description")
    private String description;

    @NotNull
    private ActivityType type;

    @NotNull
    @Min(value = 0, message = "provide valid number of days")
    private int days;
}
