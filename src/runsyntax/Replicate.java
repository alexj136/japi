package runsyntax;

import java.util.HashMap;

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

    /**
     * Alpha-convert names in a Replicate node as is required when performing
     * scope-extrusion.
     * @param from all names of this value are alpha-converted
     * @param to alpha-converted names are alpha-converted to this name
     */
    public void alphaConvert(int from, int to) {
        this.toReplicate.alphaConvert(from, to);
    }

    /**
     * Deep-copy this Replicate.
     * @return a deep-copy of this Replicate
     */
    public Replicate copy() { return new Replicate(this.toReplicate.copy()); }

    /**
     * Simple toString that just uses the integer names.
     * @return a not-very-nice string representation of this object
     */
    public String toString() { return "!" + this.toReplicate; }

    /**
     *
     */
    public String toNiceString(HashMap<Integer, String> nameMap) {
        return "! " + this.toReplicate.toNiceString(nameMap);
    }
}
