package nl.tudelft.sem.group23a.activity.models;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PeopleInHoaResponseModel {
    private List<Long> ids;
}
