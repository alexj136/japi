package syntax;

import java.util.HashSet;

public final class Variable<T> extends LambdaTerm<T> {

    private T name;

    public Variable(T name) {
        this.name = name;
    }

    public T name() { return this.name; }

    public HashSet<T> freeVars() {
        HashSet<T> fv = new HashSet<T>();
        fv.add(this.name);
        return fv;
    }

    public HashSet<T> binders() { return new HashSet<T>(); }

    public String toString() { return this.name.toString(); }

    public Variable<T> copy() { return new Variable(this.name); }
}
