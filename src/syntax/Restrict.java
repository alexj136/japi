package syntax;

/**
 * Restrict objects behave somewhat like lambda abstractions - they bind
 * occurences of names in a process.
 */
public class Restrict extends Term {

    private Name boundName;
    private Term restrictIn;

    /**
     * Construct a new Restrict object.
     * @param boundName the name to restrict/bind
     * @param restrictIn the subprocess within which to restrict the name
     * @return a new Restrict object
     */
    public Restrict(Name boundName, Term restrictIn) {
        this.boundName = boundName;
        this.restrictIn = restrictIn;
    }

    /**
     * Access the name bound in this restriction.
     * @return the name bound in this restriction
     */
    public Name getBoundName() { return this.boundName; }

    /**
     * Access the process within which this restriction binds a name.
     * @return the process within which this restriction binds a name
     */
    public Term getRestrictIn() { return this.restrictIn; }

    /**
     * Renaming of Restrict Terms is not yet implemented as I don't yet fully
     * understand their semantics.
     */
    @Override
    public void rename(Name from, Name to) throws NameRepresentationException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Obtain a pretty-printout of this Restrict.
     * @param indentationLevel the number of tabs that should appear before the
     * text
     * @return a string representing this Restrict
     */
    @Override
    public String prettyPrint(int indentationLevel) {
        return SyntaxElement.generateIndent(indentationLevel) +
                "restricting " +
                boundName.prettyPrint(indentationLevel) +
                " in\n" +
                restrictIn.prettyPrint(indentationLevel + 1);
    }
}
