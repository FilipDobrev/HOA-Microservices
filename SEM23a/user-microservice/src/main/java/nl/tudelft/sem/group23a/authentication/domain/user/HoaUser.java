package nl.tudelft.sem.group23a.authentication.domain.user;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.group23a.authentication.domain.HasEvents;
import nl.tudelft.sem.group23a.authentication.domain.builder.HoaUserBuilder;
import nl.tudelft.sem.group23a.authentication.domain.events.UserWasCreatedEvent;
import nl.tudelft.sem.group23a.authentication.domain.user.converters.EmailAttributeConverter;
import nl.tudelft.sem.group23a.authentication.domain.user.converters.EncodedPasswordAttributeConverter;
import nl.tudelft.sem.group23a.authentication.domain.user.converters.UsernameAttributeConverter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class HoaUser extends HasEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    @Convert(converter = UsernameAttributeConverter.class)
    private Username username;

    @Column(name = "password", nullable = false)
    @Convert(converter = EncodedPasswordAttributeConverter.class)
    private EncodedPassword password;

    @Column(name = "email")
    @Convert(converter = EmailAttributeConverter.class)
    private Email email;

    /**
     * Constructor used to create a user.
     *
     * @param name     the username of the user
     * @param password the password of the user
     * @param email    Optional of the users email if they give it
     */
    public HoaUser(Username name, EncodedPassword password, Email email) {
        this.username = name;
        this.password = password;
        this.email = email;
        this.recordThat(new UserWasCreatedEvent(name));
    }

    /**
     * Creates a new HoaUserBuilder.
     *
     * @return a HoaUserBuilder
     */
    public static HoaUserBuilder builder() {
        return new HoaUserBuilder();
    }
}
