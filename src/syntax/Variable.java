package syntax;

import java.util.HashSet;
import java.util.HashMap;

/**
 * Represents lambda calculus variables.
 */
public final class Variable extends LambdaTerm {

    private int name;

    /**
     * Construct a new Variable.
     * @param name the name of the Variable
     */
    public Variable(int name) {
        this.name = name;
    }

    /**
     * Access the name of this Variable.
     * @return the name of this Variable
     */
    public int name() { return this.name; }

    /**
     * Enumerate the free variable names in this Variable (which will always be
     * this Variable name only).
     * @return a HashSet of the free variable names of this Variable
     */
    public HashSet<Integer> freeVars() {
        HashSet<Integer> fv = new HashSet<Integer>();
        fv.add(this.name);
        return fv;
    }

    /**
     * Enumerate all the variable names bound in this Variable (always none).
     * @return A HashSet of all variable names bound in this Variable
     */
    public HashSet<Integer> binders() { return new HashSet<Integer>(); }

    /**
     * Obtain a String representation of this Variable.
     * @return a String representation of this Variable
     */
    public String toString() { return this.name.toString(); }

    /**
     * Obtain a String representation of this Variable, using a different type
     * for names.
     * @param nameMap map from T names to U names
     * @return a String representation of this Variable, printing the Object
     * mapped to in nameMap by the Variable name
     */
    public String toStringWithNameMap(HashMap<Integer, String> nameMap) {
        return nameMap.get(this.name).toString();
    }

    /**
     * Copy this Variable.
     * @return a copy of this Variable
     */
    public Variable copy() { return new Variable(this.name); }

    /**
     * Carelessly rename every occurence of a specific variable name within this
     * Variable.
     * @param from rename the stored variable name if it matches this name
     * @param to if renaming the stored variable, rename it to this value
     */
    public void blindRename(int from, int to) {
        if(this.name.equals(from)) { this.name = to; }
    }

    /**
     * Rename every free occurence of a specific variable name within this
     * Variable.
     * @param from rename the stored variable name if it matches this name
     * @param to if renaming the stored variable, rename it to this value
     */
    public void renameFree(int from, int to) {
        if(this.name.equals(from)) { this.name = to; }
    }

    /**
     * Rename all bound occurences of the given name within this Variable (i.e.
     * do nothing).
     * @param from rename variable names of this value
     * @param to those variable names being renamed are renamed to this value
     */
    public void renameNonFree(int from, int to) {}
}
