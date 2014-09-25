package syntax;

/**
 * Restrict objects behave somewhat like lambda abstractions - they bind
 * occurences of names in a process.
 */
public class Restrict extends SyntaxElement {

    private Name boundName;
    private SyntaxElement restrictIn;

    /**
     * Construct a new Restrict object.
     * @param boundName the name to restrict/bind
     * @param restrictIn the subprocess within which to restrict the name
     * @return a new Restrict object
     */
    public Restrict(Name boundName, SyntaxElement restrictIn) {
        this.boundName = boundName;
        this.restrictIn = restrictIn;
    }
}
