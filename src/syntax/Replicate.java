package syntax;

/**
 * Replicate elements repeatedly copy a process.
 */
public class Replicate extends Term {

    private Term toReplicate;

    /**
     * Construct a new replicating process.
     * @param toReplicate the process that will be replicated
     * @return a new Replicate object
     */
    public Replicate(Term toReplicate) {
        this.toReplicate = toReplicate;
    }

    /**
     * Access the term to be replicated.
     * @return the term to be replicated
     */
    public Term getToReplicate() { return this.toReplicate; }

    /**
     * Renaming of Replicate Terms is not yet implemented as I don't yet fully
     * understand the semantics of this operation.
     */
    @Override
    public void rename(Name from, Name to) throws NameRepresentationException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Obtain a pretty-printout of this Replicate.
     * @param indentationLevel the number of tabs that should appear before the
     * text
     * @return a string representing this Replicate
     */
    @Override
    public String prettyPrint(int indentationLevel) {
        return SyntaxElement.generateIndent(indentationLevel) +
                "repeatedly\n" +
                toReplicate.prettyPrint(indentationLevel + 1);
    }
}
