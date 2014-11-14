package syntax;

import java.util.HashSet;
import java.util.HashMap;

public final class Application<T> extends LambdaTerm<T> {

    private LambdaTerm<T> func;
    private LambdaTerm<T> arg;

    public Application(LambdaTerm<T> func, LambdaTerm<T> arg) {
        this.func = func;
        this.arg = arg;
    }

    public LambdaTerm<T> func() { return this.func; }

    public LambdaTerm<T> arg() { return this.arg; }

    public void setFunc(LambdaTerm<T> func) { this.func = func; }

    public void setArg(LambdaTerm<T> arg) { this.arg = arg; }

    public HashSet<T> freeVars() {
        HashSet<T> fvFunc = this.func.freeVars();
        HashSet<T> fvArg = this.arg.freeVars();
        for(T name : fvFunc) { fvArg.add(name); }
        return fvArg;
    }

    public HashSet<T> binders() {
        HashSet<T> bindersFunc = this.func.binders();
        HashSet<T> bindersArg = this.arg.binders();
        for(T name : bindersFunc) { bindersArg.add(name); }
        return bindersArg;
    }

    public String toString() {
        return this.func + " " + this.arg;
    }

    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        return this.func.toStringWithNameMap(nameMap) + " " +
                this.arg.toStringWithNameMap(nameMap);
    }

    public Application<T> copy() {
        return new Application(this.func.copy(), this.arg.copy());
    }

    public void blindRename(T from, T to) {
        this.func.blindRename(from, to);
        this.arg.blindRename(from, to);
    }

    public void renameNonFree(T from, T to) {
        this.func.renameNonFree(from, to);
        this.arg.renameNonFree(from, to);
    }
}
