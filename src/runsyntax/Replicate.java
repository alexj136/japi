package runsyntax;

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
     * Rename the names within this Replicate as though a message had been
     * received by a containing process
     * @param from names with this value are renamed
     * @param to names being renamed are renamed to this name
     */
    public void rename(int from, int to) {
        this.toReplicate.rename(from, to);
    }

    public String toString() { return "!" + this.toReplicate; }
}
