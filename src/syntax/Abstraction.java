package syntax;

import java.util.HashSet;
import java.util.HashMap;

/**
 * Represents a lambda calculus abstraction.
 */
public final class Abstraction<T> extends LambdaTerm<T> {

    private static String lambdaSymbol = "lambda";

    private T name;
    private LambdaTerm<T> body;

    /**
     * Construct a new Abstraction.
     * @param name the name to bind
     * @param body the body of the Abstraction
     */
    public Abstraction(T name, LambdaTerm<T> body) {
        this.name = name;
        this.body = body;
    }

    /**
     * Access the name bound by the Abstraction.
     * @return the name bound by the Abstraction
     */
    public T name() { return this.name; }

    /**
     * Access the body of the Abstraction.
     * @return the body of the Abstraction
     */
    public LambdaTerm<T> body() { return this.body; }

    /**
     * Reassign the body of the Abstraction.
     * @param body the body of the Abstraction
     */
    public void setBody(LambdaTerm<T> body) { this.body = body; }

    /**
     * Enumerate the free variables of this Abstraction.
     * @return a HashSet of the free variables of this Abstraction
     */
    public HashSet<T> freeVars() {
        HashSet<T> fv = this.body.freeVars();
        fv.remove(this.name);
        return fv;
    }

    /**
     * Enumerate all binders contained in this term including the name bound in
     * this Abstraction.
     * @return a HashSet of all binders contained in this term including the
     * name bound in this Abstraction.
     */
    public HashSet<T> binders() {
        HashSet<T> binders = this.body.binders();
        binders.add(this.name);
        return binders;
    }

    /**
     * Generate a string representation of this Abstraction.
     * @return a string representation of this Abstraction
     */
    public String toString() {
        String bodyStr = this.body.toString();
        if(bodyStr.startsWith(Abstraction.lambdaSymbol)) {
            return Abstraction.lambdaSymbol + " " + this.name + "," +
                    bodyStr.substring(Abstraction.lambdaSymbol.length());
        }
        else {
            return Abstraction.lambdaSymbol + " " + this.name + ": " +
                    this.body;
        }
    }

    /**
     * Obtain a string representation of this Abstraction, using a different
     * name type.
     * @return a string representing the Abstraction, printing names of a
     * different type, the values of which are mapped to by the contained names
     */
    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        String bodyStr = this.body.toStringWithNameMap(nameMap);
        if(bodyStr.startsWith(Abstraction.lambdaSymbol)) {
            return Abstraction.lambdaSymbol + " " + nameMap.get(this.name) +
                    "," + bodyStr.substring(Abstraction.lambdaSymbol.length());
        }
        else {
            return Abstraction.lambdaSymbol + " " + nameMap.get(this.name) +
                    ": " + this.body.toStringWithNameMap(nameMap);
        }
    }

    /**
     * Copy this Abstraction.
     * @return a copy of this Abstraction
     */
    public Abstraction<T> copy() {
        return new Abstraction(this.name, this.body.copy());
    }

    /**
     * Rename all occurrences of 'from' with 'to' in this Abstraction.
     * @param from all occurrences of this name are changed
     * @param to names being changed are replaced with this value
     */
    public void blindRename(T from, T to) {
        if(this.name.equals(from)) {
            this.name = to;
        }
        this.body.blindRename(from, to);
    }

    /**
     * Rename all free occurrences of 'from' with 'to' in this Abstraction.
     * @param from all occurrences of this name are changed
     * @param to names being changed are replaced with this value
     */
    public void renameFree(T from, T to) {
        if(!this.name.equals(from)) {
            this.body.renameFree(from, to);
        }
    }

    /**
     * Rename all bound names in this term, including the binders.
     * @param from the name to change from
     * @param to the name to change to
     */
    public void renameNonFree(T from, T to) {
        if(this.name.equals(from)) {
            this.name = to;
            this.body.blindRename(from, to);
        }
        else {
            this.body.renameNonFree(from, to);
        }
    }
}
