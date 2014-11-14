package syntax;

import java.util.HashSet;
import java.util.HashMap;

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

    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        return nameMap.get(this.name).toString();
    }

    public Variable<T> copy() { return new Variable(this.name); }

    public void blindRename(T from, T to) {
        if(this.name.equals(from)) {
            this.name = to;
        }
    }

    public void renameNonFree(T from, T to) {}
}
