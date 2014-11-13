package syntax;

import java.util.HashSet;

public final class Abstraction<T> extends LambdaTerm<T> {

    private static String lambdaSymbol = "lambda";

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

    public HashSet<T> binders() {
        HashSet<T> binders = this.body.binders();
        binders.add(this.name);
        return binders;
    }

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

    public Abstraction<T> copy() {
        return new Abstraction(this.name, this.body.copy());
    }
}
