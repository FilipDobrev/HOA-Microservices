package nl.tudelft.sem.group23a.commons;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Placeholder {
    private final boolean yes;

    /**
     * Getter for the boolean.
     * Just here to keep the pipeline from blowing up because of JaCoCo.
     *
     * @return the value of the boolean yes
     */
    public boolean isYes() {
        return this.yes;
    }
}
