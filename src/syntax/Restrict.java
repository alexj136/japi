package syntax;

import java.util.HashMap;

/**
 * Restrict objects behave somewhat like lambda abstractions - they bind
 * occurences of names in a process.
 */
public final class Restrict<T> extends PiTermOneSub<T> {

    private T boundName;

    /**
     * Construct a new Restrict object.
     * @param boundName the name to restrict/bind
     * @param subterm the subprocess within which to restrict the name
     * @return a new Restrict object
     */
    public Restrict(T boundName, PiTerm<T> subterm) {
        super(subterm);
        this.boundName = boundName;
    }

    /**
     * Access the name bound in this restriction.
     * @return the name bound in this restriction
     */
    public T boundName() { return this.boundName; }

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
    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        return "new " + nameMap.get(this.boundName) + " in " +
                this.subterm.toStringWithNameMap(nameMap);
    }

    /**
     * Renamw this Restrict as is required when a message is passed.
     * @param from Names of this value are renamed
     * @param to names being renamed are renamed to this value
     */
    public void rename(T from, T to) {
        if(!this.boundName.equals(from)) { this.subterm.rename(from, to); }
    }

    /**
     * Alpha-convert this Restrict as is required when performing scope
     * extrusion.
     * @param from Names of this value are renamed
     * @param to names being renamed are renamed to this value
     */
    public void alphaConvert(T from, T to) {
        if(this.boundName.equals(from)) {
            this.boundName = to;
        }
        this.subterm.alphaConvert(from, to);
    }

    /**
     * Copy this Restrict. No need to copy the name objects, just copy the
     * reference
     * @return a copy of this Restrict
     */
    public Restrict<T> copy() {
        return new Restrict(this.boundName, this.subterm.copy());
    }
}
