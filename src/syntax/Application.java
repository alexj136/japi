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

    public void setFunc(LambdaTerm func) { this.func = func; }

    public void setArg(LambdaTerm arg) { this.arg = arg; }

    public HashSet<Integer> freeVars() {
        HashSet<Integer> fvFunc = this.func.freeVars();
        HashSet<Integer> fvArg = this.arg.freeVars();
        for(Integer name : fvFunc) { fvArg.add(name); }
        return fvArg;
    }

    public HashSet<Integer> binders() {
        HashSet<Integer> bindersFunc = this.func.binders();
        HashSet<Integer> bindersArg = this.arg.binders();
        for(Integer name : bindersFunc) { bindersArg.add(name); }
        return bindersArg;
    }

    public String toString() {
        return "(" + this.func + " " + this.arg + ")";
    }

    public String toStringWithNameMap(HashMap<Integer, String> nameMap) {
        return "(" + this.func.toStringWithNameMap(nameMap) + " " +
                this.arg.toStringWithNameMap(nameMap) + ")";
    }

    public Application copy() {
        return new Application(this.func.copy(), this.arg.copy());
    }

    public void blindRename(int from, int to) {
        this.func.blindRename(from, to);
        this.arg.blindRename(from, to);
    }

    public void renameFree(int from, int to) {
        this.func.renameFree(from, to);
        this.arg.renameFree(from, to);
    }

    public void renameNonFree(int from, int to) {
        this.func.renameNonFree(from, to);
        this.arg.renameNonFree(from, to);
    }
}
