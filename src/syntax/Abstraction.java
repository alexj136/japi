package syntax;

import java.util.HashSet;

public final class Abstraction<T> extends LambdaTerm<T> {

    private T name;
    private LambdaTerm<T> body;

    public Abstraction(T name, LambdaTerm<T> body) {
        this.name = name;
        this.body = body;
    }

    public T name() { return this.name; }

    public LambdaTerm<T> body() { return this.body; }

    public void setBody(LambdaTerm<T> body) { this.body = body; }

    public HashSet<T> freeVars() {
        HashSet<T> fv = this.body.freeVars();
        fv.remove(this.name);
        return fv;
    }

    public Abstraction<T> copy() {
        return new Abstraction(this.name, this.body.copy());
    }
}
