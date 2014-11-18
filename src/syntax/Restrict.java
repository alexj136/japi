package syntax;

import java.util.HashMap;
import java.util.HashSet;

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
     * Enumerate the binders in this Restrict.
     * @return a HashSey of the binders in this Restrict
     */
    @Override
    public HashSet<T> binders() {
        HashSet<T> subBinders = super.binders();
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
    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        return "new " + nameMap.get(this.boundName) + " in " +
                this.subterm.toStringWithNameMap(nameMap);
    }

    /**
     * Substitute within this Restrict as is required when a message is passed.
     * @param replacing names of this value are replaced
     * @param with names being replaced are replaced with this value
     */
    public void msgPass(T replacing, LambdaTerm<T> with) {
        if(!this.boundName.equals(replacing)) {
            this.subterm.msgPass(replacing, with);
        }
    }

    /**
     * Carelessly rename this Restrict.
     * @param from all names of this value are renamed
     * @param to all names being renamed are renamed to this value
     */
    public void blindRename(T from, T to) {
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
    public Restrict<T> copy() {
        return new Restrict(this.boundName, this.subterm.copy());
    }
}
