package syntax;

import java.util.HashMap;

/**
 * Replicate elements repeatedly copy a process.
 */
public final class Replicate<T> extends TermOneSub<T> {

    /**
     * Construct a new replicating process.
     * @param subterm the process that will be replicated
     * @return a new Replicate object
     */
    public Replicate(Term<T> subterm) {
        super(subterm);
    }

    /**
     * Obtain a string representation of this Replicate.
     * @return a string representing this Replicate
     */
    @Override
    public String toString() { return "! " + this.subterm.toString(); }

    /**
     * Obtain a string representation of this Replicate, using a different name
     * type.
     * @return a string representing the Replicate, printing names of a
     * different type, the values of which are mapped to by the contained names.
     */
    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        return "! " + this.subterm.toStringWithNameMap(nameMap);
    }

    /**
     * Rename names in this Replicate as is necessary when a message is passed.
     * @param from Names of this value are renamed
     * @param to names being renamed are renamed to this value
     */
    public void rename(T from, T to) {
        this.subterm.rename(from, to);
    }

    /**
     * Alpha-convert this Replicate as is necessary when doing scope extrusion.
     * @param from Names of this value are renamed
     * @param to names being renamed are renamed to this value
     */
    public void alphaConvert(T from, T to) {
        this.subterm.alphaConvert(from, to);
    }

    /**
     * Copy this Replicate.
     * @return a copy of this Replicate
     */
    public Replicate<T> copy() { return new Replicate<T>(this.subterm.copy()); }
}
