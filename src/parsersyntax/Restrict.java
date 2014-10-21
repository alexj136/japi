package parsersyntax;

/**
 * Restrict objects behave somewhat like lambda abstractions - they bind
 * occurences of names in a process.
 */
public class Restrict extends Term {

    private String boundName;
    private Term subterm;

    /**
     * Construct a new Restrict object.
     * @param boundName the name to restrict/bind
     * @param subterm the subprocess within which to restrict the name
     * @return a new Restrict object
     */
    public Restrict(String boundName, Term subterm) {
        this.boundName = boundName;
        this.subterm = subterm;
    }

    /**
     * Access the name bound in this restriction.
     * @return the name bound in this restriction
     */
    public String boundName() { return this.boundName; }

    /**
     * Access the process within which this restriction binds a name.
     * @return the process within which this restriction binds a name
     */
    public Term subterm() { return this.subterm; }

    /**
     * Obtain a pretty-printout of this Restrict.
     * @param indentLevel the number of tabs that should appear before the text
     * @return a string representing this Restrict
     */
    @Override
    public String toString() {
        return "new " + this.boundName + " in " + this.subterm.toString();
    }
}
