package syntax;

import java.util.HashSet;
import java.util.HashMap;

/**
 * Represents lambda calculus function application.
 */
public final class Application extends LambdaTerm {

    private LambdaTerm func;
    private LambdaTerm arg;

    /**
     * Construct a new Application.
     * @param func the function (LHS term) of the Application
     * @param arg the argument (RHS term) of the Application
     * @return The application of func to arg as a LambdaTerm
     */
    public Application(LambdaTerm func, LambdaTerm arg) {
        this.func = func;
        this.arg = arg;
    }

    /**
     * Access the function (LHS term) of the Application.
     * @return the function (LHS term) of the Application
     */
    public LambdaTerm func() { return this.func; }

    /**
     * Access the argument (RHS term) of the Application.
     * @return the argument (RHS term) of the Application
     */
    public LambdaTerm arg() { return this.arg; }

    /**
     * Set the func field of this Application.
     * @param func the new function
     */
    public void setFunc(LambdaTerm func) { this.func = func; }

    /**
     * Set the arg field of this Application.
     * @param arg the new argument
     */
    public void setArg(LambdaTerm arg) { this.arg = arg; }

    /**
     * Enumerate the free variables of this Application.
     * @return a HashSet of the free variables of this Application
     */
    public HashSet<Integer> freeVars() {
        HashSet<Integer> fvFunc = this.func.freeVars();
        HashSet<Integer> fvArg = this.arg.freeVars();
        for(Integer name : fvFunc) { fvArg.add(name); }
        return fvArg;
    }

    /**
     * Enumerate the binders and bound variable names of this Application.
     * @return a HashSet of the binders and bound variable names of this
     * Application
     */
    public HashSet<Integer> binders() {
        HashSet<Integer> bindersFunc = this.func.binders();
        HashSet<Integer> bindersArg = this.arg.binders();
        for(Integer name : bindersFunc) { bindersArg.add(name); }
        return bindersArg;
    }

    /**
     * Replace every occurrence of the name {@code from} with {@code to} in this
     * Application.
     * @param from the name to be replaced
     * @param to the name to replace with
     */
    public void blindRename(Integer from, Integer to) {
        this.func.blindRename(from, to);
        this.arg.blindRename(from, to);
    }

    /**
     * Replace every free occurrence of the name {@code from} with {@code to} in
     * this Application.
     * @param from the name to be replaced
     * @param to the name to replace with
     */
    public void renameFree(Integer from, Integer to) {
        this.func.renameFree(from, to);
        this.arg.renameFree(from, to);
    }

    /**
     * Replace every bound or binding occurrence of the name {@code from} with
     * {@code to} in this Application.
     * @param from the name to be replaced
     * @param to the name to replace with
     */
    public void renameNonFree(Integer from, Integer to) {
        this.func.renameNonFree(from, to);
        this.arg.renameNonFree(from, to);
    }

    /**
     * Obtain a String representation of this Application, using the integer
     * names generated during lexical analysis.
     * @return a String representation of this Application
     */
    public String toString() {
        return "(" + this.func + " " + this.arg + ")";
    }

    /**
     * Obtain a String representation of this Application, using the user's
     * identifier Strings
     * @return a String representation of this Application
     */
    public String toStringWithNameMap(HashMap<Integer, String> nameMap) {
        return "(" + this.func.toStringWithNameMap(nameMap) + " " +
                this.arg.toStringWithNameMap(nameMap) + ")";
    }

    /**
     * Deep-copy this Application.
     * @return a deep-copy of this Application.
     */
    public Application copy() {
        return new Application(this.func.copy(), this.arg.copy());
    }
}
