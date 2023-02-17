package nl.tudelft.sem.group23a.authentication.domain.builder;

import lombok.Data;
import nl.tudelft.sem.group23a.authentication.domain.user.Email;
import nl.tudelft.sem.group23a.authentication.domain.user.EncodedPassword;
import nl.tudelft.sem.group23a.authentication.domain.user.HoaUser;
import nl.tudelft.sem.group23a.authentication.domain.user.Username;
import nl.tudelft.sem.group23a.commons.exceptions.NotAllNecessaryFieldsAddedException;

@Data
public class UserDirector {
    private final HoaUserBuilder builder;

    /**
     * Used to return a basic user with only the necessary fields set.
     *
     * @return the user with the essential fields only
     * @throws NotAllNecessaryFieldsAddedException Won't be thrown in this case as everything is set by hand
     */
    public HoaUser basicUser() throws NotAllNecessaryFieldsAddedException {
        return builder.username(new Username("Default"))
                .password(new EncodedPassword("DefaultEncodedPassword"))
                .build();
    }

    /**
     * Used to return a full user with all the fields set.
     *
     * @return the user with all the optional fields
     * @throws NotAllNecessaryFieldsAddedException Won't be thrown in this case as everything is set by hand
     */
    public HoaUser fullUser() throws NotAllNecessaryFieldsAddedException {
        return builder.username(new Username("Default"))
                .password(new EncodedPassword("DefaultEncodedPassword"))
                .email(new Email("default@mail.nl"))
                .build();
    }
}
