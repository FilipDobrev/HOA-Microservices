package nl.tudelft.sem.group23a.authentication.domain.user.converters;

import javax.persistence.AttributeConverter;
import nl.tudelft.sem.group23a.authentication.domain.user.Email;

public class EmailAttributeConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email attribute) {
        return attribute.toString();
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        return new Email(dbData);
    }
}
