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

    @Override
    public void rename(Name from, Name to) throws NameRepresentationException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String prettyPrint(int indentationLevel) {
        return SyntaxElement.generateIndent(indentationLevel) +
                "restricting " +
                boundName.prettyPrint(indentationLevel) +
                " in\n" +
                restrictIn.prettyPrint(indentationLevel + 1);
    }
}