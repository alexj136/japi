package runsyntax;

import java.util.HashMap;

/**
 * A Term object represents any pi calculus term, with names as integers.
 */
public abstract class Term {

    /**
     * Rename the names in a Term as is necessary after the exchange of a
     * message - this is not alpha-conversion.
     * @param from some names of this value must be renamed
     * @param to names being renamed are renamed to this value
     */
    public abstract void rename(int from, int to);

    /**
     * Rename every single occurence of the first given name with the second
     * given name.
     * @param from all names of this value must be renamed
     * @param to names being renamed are renamed to this value
     */
    public abstract void alphaConvert(int from, int to);

    /**
     * Deep-copy a Term.
     * @return a deep-copy of this Term
     */
    public abstract Term copy();

    /**
     * Force subclasses to override toString. This is not supposed to be a
     * particularly nice pretty-print, just something simple to use for casual
     * tests.
     * @return a string representation of this Term
     */
    @Override
    public abstract String toString();

    /**
     * Prettier stringification method that converts variable names back to
     * those given by the user.
     * @param nameMap a HashMap used to obtain user variable names from
     * interpreter ones
     * @return a string representation of this Term
     */
    public abstract String toNiceString(HashMap<Integer, String> nameMap);
}
