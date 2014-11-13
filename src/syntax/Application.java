package syntax;

import java.util.HashSet;

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

    public Application<T> copy() {
        return new Application(this.func.copy(), this.arg.copy());
    }
}
