package syntax;

import java.util.HashMap;

/**
 * Replicate elements repeatedly copy a process.
 */
public final class Replicate<T> extends PiTermOneSub<T> {

    /**
     * Construct a new replicating process.
     * @param subterm the process that will be replicated
     * @return a new Replicate object
     */
    public Replicate(PiTerm<T> subterm) {
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
     * Substitute LambdaTerms for names in this Replicate as is necessary when a
     * message is passed.
     * @param replacing names of this value are replaced
     * @param to names being replaced are replaced with this value
     */
    public void msgPass(T replacing, LambdaTerm<T> with) {
        this.subterm.msgPass(replacing, with);
    }

    /**
     * Carelessly rename this Replicate.
     * @param from all names of this value are renamed
     * @param to all names being renamed are renamed to this value
     */
    public void blindRename(T from, T to) {
        this.subterm.blindRename(from, to);
    }

    /**
     * Copy this Replicate.
     * @return a copy of this Replicate
     */
    public Replicate<T> copy() { return new Replicate<T>(this.subterm.copy()); }
}
