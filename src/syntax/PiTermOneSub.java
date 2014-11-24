package syntax;

import java.util.HashSet;

/**
 * Represents at a higher level those terms that have a single subterm. This
 * includes Send, Receive, Replicate and Restrict.
 */
public abstract class PiTermOneSub extends PiTerm {

    protected PiTerm subterm;

    /**
     * Construct a new PiTermOneSub.
     * @param subterm The subterm of this PiTerm
     */
    public PiTermOneSub(PiTerm subterm) { this.subterm = subterm; }

    /**
     * Access the stored subterm.
     * @return the stored subterm
     */
    public PiTerm subterm() { return this.subterm; }

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
}
