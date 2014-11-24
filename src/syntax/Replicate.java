package syntax;

import java.util.HashMap;
import java.util.HashSet;

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
     * Enumerate the binders in this Replicate.
     * @return a HashSet of the binders in this Replicate
     */
    public HashSet<Integer> binders() { return this.subterm.binders(); }

    /**
     * Enumerate the free variable names in this Replicate.
     * @return a HashSet of the free variable names in this Replicate
     */
    public HashSet<Integer> freeVars() { return this.subterm.freeVars(); }

    /**
     * Rename the free occurrences of 'from' to 'to' in this Replicate.
     * @param from all free occurrences of this name are changed
     * @param to names being changed are replaced with this value
     */
    public void renameFree(Integer from, Integer to) {
        this.subterm.renameFree(from, to);
    }

    /**
     * Rename the binding and bound occurrences of 'from' to 'to' in this
     * Replicate.
     * @param from all binding and bound occurrences of this name are changed
     * @param to names being changed are replaced with this value
     */
    public void renameNonFree(Integer from, Integer to) {
        this.subterm.renameNonFree(from, to);
    }

    /**
     * Carelessly rename this Replicate.
     * @param from all names of this value are renamed
     * @param to all names being renamed are renamed to this value
     */
    public void blindRename(Integer from, Integer to) {
        this.subterm.blindRename(from, to);
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
