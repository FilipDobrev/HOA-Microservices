package nl.tudelft.sem.group23a.activity.models;

import static lombok.AccessLevel.PROTECTED;

import java.util.Calendar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ActivityModel {
    private long id;
    private long hoaId;
    private String description;
    private Calendar timestamp;
    private String[] choices;
}
