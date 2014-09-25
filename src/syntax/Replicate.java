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
}
