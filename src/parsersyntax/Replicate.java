package parsersyntax;

/**
 * Replicate elements repeatedly copy a process.
 */
public class Replicate extends TermOneSub {

    /**
     * Construct a new replicating process.
     * @param subterm the process that will be replicated
     * @return a new Replicate object
     */
    public Replicate(Term subterm) {
        super(subterm);
    }

    /**
     * Obtain a string representation of this Replicate.
     * @return a string representing this Replicate
     */
    @Override
    public String toString() { return "! " + this.subterm.toString(); }
}
