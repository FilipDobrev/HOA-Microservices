package nl.tudelft.sem.group23a.commons.builder;

import nl.tudelft.sem.group23a.commons.exceptions.NotAllNecessaryFieldsAddedException;

public interface Builder<T> {
    public T build() throws NotAllNecessaryFieldsAddedException;
}
