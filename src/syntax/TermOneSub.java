package syntax;

/**
 * Represents at a higher level those terms that have a single subterm. This
 * includes Send, Receive, Replicate and Restrict.
 */
public abstract class TermOneSub<T> extends Term<T> {

    protected Term<T> subterm;

    /**
     * Construct a new TermOneSub.
     * @param subterm The subterm of this Term
     */
    public TermOneSub(Term<T> subterm) { this.subterm = subterm; }

    /**
     * Access the stored subterm.
     * @return the stored subterm
     */
    public Term<T> subterm() { return this.subterm; }
}
