package syntax;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Restrict objects behave somewhat like lambda abstractions - they bind
 * occurences of names in a process.
 */
public final class Restrict extends PiTermOneSub {

    private Integer boundName;

    /**
     * Construct a new Restrict object.
     * @param boundName the name to restrict/bind
     * @param subterm the subprocess within which to restrict the name
     * @return a new Restrict object
     */
    public Restrict(Integer boundName, PiTerm subterm) {
        super(subterm);
        this.boundName = boundName;
    }

    /**
     * Access the name bound in this restriction.
     * @return the name bound in this restriction
     */
    public Integer boundName() { return this.boundName; }

    /**
     * Enumerate the binders in this Restrict.
     * @return a HashSey of the binders in this Restrict
     */
    @Override
    public HashSet<Integer> binders() {
        HashSet<Integer> subBinders = super.binders();
        subBinders.add(this.boundName());
        return subBinders;
    }

    /**
     * Obtain a pretty-printout of this Restrict.
     * @param indentLevel the number of tabs that should appear before the text
     * @return a string representing this Restrict
     */
    @Override
    public String toString() {
        return "new " + this.boundName.toString() + " in " +
                this.subterm.toString();
    }

    /**
     * Obtain a string representation of this Restrict, using a different name
     * type.
     * @return a string representing the Restrict, printing names of a
     * different type, the values of which are mapped to by the contained names.
     */
    public String toStringWithNameMap(HashMap<Integer, String> nameMap) {
        return "new " + nameMap.get(this.boundName) + " in " +
                this.subterm.toStringWithNameMap(nameMap);
    }

    /**
     * Carelessly rename this Restrict.
     * @param from all names of this value are renamed
     * @param to all names being renamed are renamed to this value
     */
    public void blindRename(Integer from, Integer to) {
        if(this.boundName.equals(from)) {
            this.boundName = to;
        }
        this.subterm.blindRename(from, to);
    }

    /**
     * Copy this Restrict. No need to copy the name objects, just copy the
     * reference
     * @return a copy of this Restrict
     */
    public Restrict copy() {
        return new Restrict(this.boundName, this.subterm.copy());
    }
}
