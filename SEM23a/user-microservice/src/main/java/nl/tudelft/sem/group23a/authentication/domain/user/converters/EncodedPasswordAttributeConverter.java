package nl.tudelft.sem.group23a.authentication.domain.user.converters;

import javax.persistence.AttributeConverter;
import nl.tudelft.sem.group23a.authentication.domain.user.EncodedPassword;

public class EncodedPasswordAttributeConverter implements AttributeConverter<EncodedPassword, String> {

    @Override
    public String convertToDatabaseColumn(EncodedPassword attribute) {
        return attribute.toString();
    }

    @Override
    public EncodedPassword convertToEntityAttribute(String dbData) {
        return new EncodedPassword(dbData);
    }
}
