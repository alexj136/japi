package syntax;

/**
 * Replicate elements repeatedly copy a process.
 */
public final class Replicate<T> extends TermOneSub<T> {

    /**
     * Construct a new replicating process.
     * @param subterm the process that will be replicated
     * @return a new Replicate object
     */
    public Replicate(Term<T> subterm) {
        super(subterm);
    }

    /**
     * Obtain a string representation of this Replicate.
     * @return a string representing this Replicate
     */
    @Override
    public String toString() { return "! " + this.subterm.toString(); }
}
