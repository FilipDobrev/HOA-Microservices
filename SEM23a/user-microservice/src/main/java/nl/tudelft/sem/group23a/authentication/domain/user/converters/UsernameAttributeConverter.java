package nl.tudelft.sem.group23a.authentication.domain.user.converters;

import javax.persistence.AttributeConverter;
import nl.tudelft.sem.group23a.authentication.domain.user.Username;

public class UsernameAttributeConverter implements AttributeConverter<Username, String> {

    @Override
    public String convertToDatabaseColumn(Username attribute) {
        return attribute.toString();
    }

    @Override
    public Username convertToEntityAttribute(String dbData) {
        return new Username(dbData);
    }
}
