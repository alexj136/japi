package syntax;

import java.util.HashMap;

/**
 * Replicate elements repeatedly copy a process.
 */
public final class Replicate extends PiTermOneSub {

    /**
     * Construct a new replicating process.
     * @param subterm the process that will be replicated
     * @return a new Replicate object
     */
    public Replicate(PiTerm subterm) {
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
    public String toStringWithNameMap(HashMap<Integer, String> nameMap) {
        return "! " + this.subterm.toStringWithNameMap(nameMap);
    }

    /**
     * Copy this Replicate.
     * @return a copy of this Replicate
     */
    public Replicate copy() { return new Replicate(this.subterm.copy()); }
}
