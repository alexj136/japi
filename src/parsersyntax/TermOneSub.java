package parsersyntax;

/**
 * Represents at a higher level those terms that have a single subterm. This
 * includes Send, Receive, Replicate and Restrict.
 */
public abstract class TermOneSub extends Term {

    private Term subterm;

    /**
     * Construct a new TermOneSub.
     * @param subterm The subterm of this Term
     */
    public TermOneSub(Term subterm) { this.subterm = subterm; }

    /**
     * Access the stored subterm.
     * @return the stored subterm
     */
    public Term subterm() { return this.subterm; }

    /**
     * Must be defined in the concrete classes.
     * @return a string representation of this TermOneSub
     */
    public abstract String toString();
}
