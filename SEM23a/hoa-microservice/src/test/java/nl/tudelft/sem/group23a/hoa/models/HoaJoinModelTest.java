package nl.tudelft.sem.group23a.hoa.models;

import static org.assertj.core.api.Assertions.assertThat;

import nl.tudelft.sem.group23a.hoa.domain.hoa.SpecificAddress;
import org.junit.jupiter.api.Test;

public class HoaJoinModelTest {

    @Test
    public void createFromRequestReturnsNonNull() {
        HoaJoinModel model = new HoaJoinModel(
                "Country", "City", "4855", "Street", "52");

        assertThat(HoaJoinModel.createFromRequest(model)).isNotNull();
    }

    @Test
    public void createFromRequestReturnsCorrectly() {
        String country = "Netherlands";
        String city = "Delft";
        String postalCode = "2586WA";
        String street = "Street";
        String houseNumber = "86";
        HoaJoinModel model = new HoaJoinModel(country, city, postalCode, street, houseNumber);

        SpecificAddress result = HoaJoinModel.createFromRequest(model);

        assertThat(result.getCountry()).isEqualTo(country);
        assertThat(result.getCity()).isEqualTo(city);
        assertThat(result.getPostalCode()).isEqualTo(postalCode);
        assertThat(result.getStreet()).isEqualTo(street);
        assertThat(result.getHouseNumber()).isEqualTo(houseNumber);
    }
}
