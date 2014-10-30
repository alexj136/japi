package syntax;

/**
 * Represents at a higher level those terms that have a single subterm. This
 * includes Send, Receive, Replicate and Restrict.
 */
public abstract class PiTermOneSub<T> extends PiTerm<T> {

    protected PiTerm<T> subterm;

    /**
     * Construct a new PiTermOneSub.
     * @param subterm The subterm of this PiTerm
     */
    public PiTermOneSub(PiTerm<T> subterm) { this.subterm = subterm; }

    /**
     * Access the stored subterm.
     * @return the stored subterm
     */
    public PiTerm<T> subterm() { return this.subterm; }
}
