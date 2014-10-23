package syntax;

/**
 * Restrict objects behave somewhat like lambda abstractions - they bind
 * occurences of names in a process.
 */
public final class Restrict<T> extends TermOneSub<T> {

    private T boundName;

    /**
     * Construct a new Restrict object.
     * @param boundName the name to restrict/bind
     * @param subterm the subprocess within which to restrict the name
     * @return a new Restrict object
     */
    public Restrict(T boundName, Term<T> subterm) {
        super(subterm);
        this.boundName = boundName;
    }

    /**
     * Access the name bound in this restriction.
     * @return the name bound in this restriction
     */
    public T boundName() { return this.boundName; }

    /**
     * Obtain a pretty-printout of this Restrict.
     * @param indentLevel the number of tabs that should appear before the text
     * @return a string representing this Restrict
     */
    @Override
    public String toString() {
        return "new " + this.boundName.toString() + " in " +
                this.subterm.toString();
    }
}
