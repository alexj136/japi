package runsyntax;

/**
 * Represents name restriction within a process - creates a new name whose scope
 * is limited to the contained term.
 */
public class Restrict extends Term {

    private int boundName;
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

    /**
     * Deep-copy this Restrict.
     * @return a deep-copy of this Restrict
     */
    public Restrict copy() {
        return new Restrict(this.boundName, this.restrictIn.copy());
    }

    public String toString() {
        return "new c" + this.boundName + " in " + this.restrictIn;
    }
}
