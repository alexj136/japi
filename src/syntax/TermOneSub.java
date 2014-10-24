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

    /**
     * Must be defined in the concrete classes.
     * @return a string representation of this TermOneSub
     */
    public abstract String toString();

    /**
     * Rename the names in a Term as is necessary after the exchange of a
     * message - this is not alpha-conversion.
     * @param from some names of this value must be renamed
     * @param to names being renamed are renamed to this value
     */
    public abstract void rename(T from, T to);

    /**
     * Rename every single occurence of the first given name with the second
     * given name.
     * @param from all names of this value must be renamed
     * @param to names being renamed are renamed to this value
     */
    public abstract void alphaConvert(T from, T to);

    /**
     * Deep-copy a TermOneSub.
     * @return a deep-copy of this TermOneSub
     */
    public abstract TermOneSub<T> copy();
}
