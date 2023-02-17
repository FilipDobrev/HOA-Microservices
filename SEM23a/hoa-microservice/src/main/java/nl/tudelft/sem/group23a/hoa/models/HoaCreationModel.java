package nl.tudelft.sem.group23a.hoa.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * DDD Model for the creation of a HOA.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoaCreationModel {

    @Length(max = 128)
    public String name;
    @Length(max = 128)
    public String country;
    @Length(max = 128)
    public String city;
}
