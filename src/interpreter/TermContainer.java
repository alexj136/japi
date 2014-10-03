package interpreter;

import syntax.Term;

/**
 * A TermContainer is a wrapper for Terms, and is used by the interpreter.
 */
public class TermContainer {

    private Term term;

    /**
     * Construct a new TermContainer with a given Term.
     * @param term the Term to store
     * @return a TermContainer containing the given Term
     */
    public TermContainer(Term term) {
        this.term = term;
    }

    /**
     * Access the contained Term.
     * @return the contained Term
     */
    public Term get() { return this.term; }

    /**
     * Set the contained Term.
     * @param term the new Term to store
     */
    public void set(Term term) { this.term = term; }
}
