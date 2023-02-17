package nl.tudelft.sem.group23a.authentication.domain.builder;

import lombok.NoArgsConstructor;
import nl.tudelft.sem.group23a.authentication.domain.user.Email;
import nl.tudelft.sem.group23a.authentication.domain.user.EncodedPassword;
import nl.tudelft.sem.group23a.authentication.domain.user.HoaUser;
import nl.tudelft.sem.group23a.authentication.domain.user.Username;
import nl.tudelft.sem.group23a.commons.builder.Builder;
import nl.tudelft.sem.group23a.commons.exceptions.NotAllNecessaryFieldsAddedException;

@NoArgsConstructor
public class HoaUserBuilder implements Builder<HoaUser> {
    private Username name;
    private EncodedPassword passwrd;
    private Email eml;

    public HoaUserBuilder username(Username name) {
        this.name = name;
        return this;
    }

    public HoaUserBuilder password(EncodedPassword pass) {
        passwrd = pass;
        return this;
    }

    public HoaUserBuilder email(Email mail) {
        eml = mail;
        return this;
    }

    /**
     * Builds the HoaUser based on the set fields.
     *
     * @return a HoaUser object
     * @throws NotAllNecessaryFieldsAddedException when one of the necessary fields was not filled in
     */
    public HoaUser build() throws NotAllNecessaryFieldsAddedException {
        if (name == null) {
            throw new NotAllNecessaryFieldsAddedException("Username");
        }
        if (passwrd == null) {
            throw new NotAllNecessaryFieldsAddedException("Encoded password");
        }
        return new HoaUser(name, passwrd, eml);
    }
}
