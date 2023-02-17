package nl.tudelft.sem.group23a.commons.exceptions;

public class NotAllNecessaryFieldsAddedException extends Exception {
    static final long serialVersionUID = -3387516993124229947L;

    public NotAllNecessaryFieldsAddedException(String missing) {
        super(missing + " was not added to the builder, and build() cannot commence!");
    }
}
