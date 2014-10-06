package parsersyntax;

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
     * Obtain a pretty-printout of this Replicate.
     * @param indentLevel the number of tabs that should appear before the
     * text
     * @return a string representing this Replicate
     */
    @Override
    public String prettyPrint(int indentLevel) {
        return Term.indent(indentLevel) + "repeatedly\n" +
                toReplicate.prettyPrint(indentLevel + 1);
    }
}
