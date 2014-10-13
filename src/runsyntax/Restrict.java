package runsyntax;

import java.util.HashMap;

/**
 * Represents name restriction within a process - creates a new name whose scope
 * is limited to the contained term.
 */
public class Restrict extends Term {

    private int boundName;
    private Term restrictIn;

    /**
     * Construct a new Restrict object.
     * @param boundName the name to restrict/bind
     * @param restrictIn the subprocess within which to restrict the name
     * @return a new Restrict object
     */
    public Restrict(int boundName, Term restrictIn) {
        this.boundName = boundName;
        this.restrictIn = restrictIn;
    }

    /**
     * Access the name bound in this restriction.
     * @return the name bound in this restriction
     */
    public int getBoundName() { return this.boundName; }

    /**
     * Access the process within which this restriction binds a name.
     * @return the process within which this restriction binds a name
     */
    public Term getRestrictIn() { return this.restrictIn; }

    /**
     * Rename this Restrict Term as when a message is passed - With restriction,
     * the renaming continues through the term unless the name being changed
     * matches the restricted name, in which case, nothing happens.
     */
    public void rename(int from, int to) {
        if(this.boundName != from) { this.restrictIn.rename(from, to); }
    }

    /**
     * Alpha-convert names in a Restrict node as is required when performing
     * scope-extrusion.
     * @param from all names of this value are alpha-converted
     * @param to alpha-converted names are alpha-converted to this name
     */
    public void alphaConvert(int from, int to) {
        if(this.boundName == from) { this.boundName = to; }
        this.restrictIn.alphaConvert(from, to);
    }

    /**
     * Deep-copy this Restrict.
     * @return a deep-copy of this Restrict
     */
    public Restrict copy() {
        return new Restrict(this.boundName, this.restrictIn.copy());
    }

    /**
     * Simple toString that just uses the integer names.
     * @return a not-very-nice string representation of this object
     */
    public String toString() {
        return "new c" + this.boundName + " in " + this.restrictIn;
    }

    /**
     *
     */
    public String toNiceString(HashMap<Integer, String> nameMap) {
        return "new " + nameMap.get(this.boundName) + " in " +
                this.restrictIn.toNiceString(nameMap);
    }
}
