package runsyntax;

/**
 * A Term object represents any pi calculus term, with names as integers.
 */
public abstract class Term {

    /**
     * Rename the names in a Term as is necessary after the exchange of a
     * message - this is not alpha-conversion.
     * @param from names of this value must be renamed
     * @param to names being renamed are renamed to this value
     */
    public abstract void rename(int from, int to);

    public abstract Term copy();
}
