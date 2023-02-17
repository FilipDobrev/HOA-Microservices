package nl.tudelft.sem.group23a.hoa.models;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DDD Model for getting the retrieval of the members of a HOA.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PeopleInHoaResponseModel {
    private List<String> ids;
}
