package nl.tudelft.sem.group23a.activity.models;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for username and their choice.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChoiceRequestModel {
    @NotNull
    private String choice;
    @NotNull
    private String username;
}
