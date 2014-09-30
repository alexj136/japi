package syntax;

/**
 * Replicate elements repeatedly copy a process.
 */
public class Replicate extends SyntaxElement {

    private SyntaxElement toReplicate;

    /**
     * Construct a new replicating process.
     * @param toReplicate the process that will be replicated
     * @return a new Replicate object
     */
    public Replicate(SyntaxElement toReplicate) {
        this.toReplicate = toReplicate;
    }

    @Override
    public void rename(Name from, Name to) throws NameRepresentationException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String prettyPrint(int indentationLevel) {
        return SyntaxElement.generateIndent(indentationLevel) +
                "repeatedly\n" +
                toReplicate.prettyPrint(indentationLevel + 1);
    }
}
