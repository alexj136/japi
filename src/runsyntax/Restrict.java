package parsersyntax;

/**
 * Restrict objects behave somewhat like lambda abstractions - they bind
 * occurences of names in a process.
 */
public class Restrict extends Term {

    private bound boundName;
    private Term restrictIn;

    /**
     * Construct a new Restrict object.
     * @param boundName the name to restrict/bind
     * @param restrictIn the subprocess within which to restrict the name
     * @return a new Restrict object
     */
    public Restrict(int boundName, Term restrictIn) {
        this.boundName = boundName;
        this.restrictIn = restrictIn;
    }

    /**
     * Access the name bound in this restriction.
     * @return the name bound in this restriction
     */
    public int getBoundName() { return this.boundName; }

    /**
     * Access the process within which this restriction binds a name.
     * @return the process within which this restriction binds a name
     */
    public Term getRestrictIn() { return this.restrictIn; }

    /**
     * Rename this Restrict Term as when a message is passed - With restriction,
     * the renaming continues through the term unless the name being changed
     * matches the restricted name, in which case, nothing happens.
     */
    public void rename(int from, int to) {
        if(this.boundName != from) { this.restrictIn.rename(from, to); }
    }
}
