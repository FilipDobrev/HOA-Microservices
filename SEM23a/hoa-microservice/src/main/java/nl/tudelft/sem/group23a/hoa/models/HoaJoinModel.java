package nl.tudelft.sem.group23a.hoa.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group23a.hoa.domain.hoa.SpecificAddress;

/**
 * DDD Model for joining a HOA.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoaJoinModel {
    private String country;
    private String city;
    private String postalCode;
    private String street;
    private String houseNumber;

    /**
     * Creates a new SpecificAddress.
     *
     * @param joinRequest the information used to create the object
     * @return the newly created SpecificAddress
     */
    public static SpecificAddress createFromRequest(HoaJoinModel joinRequest) {
        return new SpecificAddress(joinRequest.getCountry(),
                joinRequest.getCity(),
                joinRequest.getPostalCode(),
                joinRequest.getStreet(),
                joinRequest.getHouseNumber());
    }
}
