package nl.tudelft.sem.group23a.commons;

import java.util.Calendar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponseModel {

    private Long id;

    private String description;

    private Calendar timestamp;

    private ActivityType type;

    @Override
    public String toString() {
        return "ActivityResponseModel("
                + "id="
                + id
                + ", description="
                + description
                + ", timestamp="
                + timestamp.get(Calendar.DAY_OF_MONTH)
                + "/"
                + timestamp.get(Calendar.MONTH)
                + "/"
                + timestamp.get(Calendar.YEAR)
                + "/"
                + timestamp.get(Calendar.HOUR_OF_DAY)
                + ", type="
                + type
                + ')';
    }
}
