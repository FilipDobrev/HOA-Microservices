package nl.tudelft.sem.group23a.notifications.models;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailResponseModel {
    @NotNull
    private String email;
}
