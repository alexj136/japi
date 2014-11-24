package syntax;

import java.util.HashSet;
import java.util.HashMap;

/**
 * Represents a lambda calculus abstraction.
 */
public final class Abstraction extends LambdaTerm {

    private Integer name;
    private LambdaTerm body;

    /**
     * Construct a new Abstraction.
     * @param name the name to bind
     * @param body the body of the Abstraction
     */
    public Abstraction(Integer name, LambdaTerm body) {
        this.name = name;
        this.body = body;
    }

    /**
     * Access the name bound by the Abstraction.
     * @return the name bound by the Abstraction
     */
    public Integer name() { return this.name; }

    /**
     * Access the body of the Abstraction.
     * @return the body of the Abstraction
     */
    public LambdaTerm body() { return this.body; }

    /**
     * Reassign the body of the Abstraction.
     * @param body the body of the Abstraction
     */
    public void setBody(LambdaTerm body) { this.body = body; }

    /**
     * Enumerate the free variables of this Abstraction.
     * @return a HashSet of the free variables of this Abstraction
     */
    public HashSet<Integer> freeVars() {
        HashSet<Integer> fv = this.body.freeVars();
        fv.remove(this.name);
        return fv;
    }

    /**
     * Enumerate all binders contained in this term including the name bound in
     * this Abstraction.
     * @return a HashSet of all binders contained in this term including the
     * name bound in this Abstraction.
     */
    public HashSet<Integer> binders() {
        HashSet<Integer> binders = this.body.binders();
        binders.add(this.name);
        return binders;
    }

    /**
     * Generate a string representation of this Abstraction.
     * @return a string representation of this Abstraction
     */
    public String toString() {
        return "(" + LambdaTerm.LAM + this.name.toString() + " " +
                LambdaTerm.DOT + " " + this.body.toString() + ")";
    }

    /**
     * Obtain a string representation of this Abstraction, using a different
     * name type.
     * @return a string representing the Abstraction, printing names of a
     * different type, the values of which are mapped to by the contained names
     */
    public String toStringWithNameMap(HashMap<Integer, String> nameMap) {
        return "(" + LambdaTerm.LAM + nameMap.get(this.name).toString() + " " +
                LambdaTerm.DOT + " " + this.body.toStringWithNameMap(nameMap) +
                ")";
    }

    /**
     * Copy this Abstraction.
     * @return a copy of this Abstraction
     */
    public Abstraction copy() {
        return new Abstraction(this.name, this.body.copy());
    }

    /**
     * Rename all occurrences of 'from' with 'to' in this Abstraction.
     * @param from all occurrences of this name are changed
     * @param to names being changed are replaced with this value
     */
    public void blindRename(Integer from, Integer to) {
        if(this.name.equals(from)) {
            this.name = to;
        }
        this.body.blindRename(from, to);
    }

    /**
     * Rename all free occurrences of 'from' with 'to' in this Abstraction.
     * @param from all free occurrences of this name are changed
     * @param to names being changed are replaced with this value
     */
    public void renameFree(Integer from, Integer to) {
        if(!this.name.equals(from)) {
            this.body.renameFree(from, to);
        }
    }

    /**
     * Rename all bound names in this term, including the binders.
     * @param from the name to change from
     * @param to the name to change to
     */
    public void renameNonFree(Integer from, Integer to) {
        if(this.name.equals(from)) {
            this.name = to;
            this.body.blindRename(from, to);
        }
        else {
            this.body.renameNonFree(from, to);
        }
    }
}
