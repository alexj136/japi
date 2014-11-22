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
     * Enumerate the binders in this PiTermOneSub.
     * @return a HashSet of the binders in this PiTermOneSub
     */
    public HashSet<Integer> binders() {
        return this.subterm.binders();
    }
}
