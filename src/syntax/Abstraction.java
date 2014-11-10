package syntax;

public final class Abstraction<T> extends LambdaTerm<T> {

    private T name;
    private LambdaTerm<T> body;

    public Abstraction(T name, LambdaTerm<T> body) {
        this.name = name;
        this.body = body;
    }
}
