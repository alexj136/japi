package parsersyntax;

/**
 * Restrict objects behave somewhat like lambda abstractions - they bind
 * occurences of names in a process.
 */
public class Restrict extends Term {

    private String boundName;
    private Term restrictIn;

    /**
     * Construct a new Restrict object.
     * @param boundName the name to restrict/bind
     * @param restrictIn the subprocess within which to restrict the name
     * @return a new Restrict object
     */
    public Restrict(String boundName, Term restrictIn) {
        this.boundName = boundName;
        this.restrictIn = restrictIn;
    }

    /**
     * Access the name bound in this restriction.
     * @return the name bound in this restriction
     */
    public String getBoundName() { return this.boundName; }

    /**
     * Access the process within which this restriction binds a name.
     * @return the process within which this restriction binds a name
     */
    public Term getRestrictIn() { return this.restrictIn; }

    /**
     * Obtain a pretty-printout of this Restrict.
     * @param indentLevel the number of tabs that should appear before the text
     * @return a string representing this Restrict
     */
    @Override
    public String prettyPrint(int indentLevel) {
        return Term.indent(indentLevel) + "restricting " +
                boundName.prettyPrint(indentLevel) + " in\n" +
                restrictIn.prettyPrint(indentLevel + 1);
    }
}
